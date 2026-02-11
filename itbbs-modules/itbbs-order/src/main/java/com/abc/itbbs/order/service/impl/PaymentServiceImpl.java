package com.abc.itbbs.order.service.impl;

import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.order.config.AlipayConfig;
import com.abc.itbbs.order.domain.dto.PaymentAsyncDTO;
import com.abc.itbbs.order.domain.entity.Order;
import com.abc.itbbs.order.domain.enums.OrderBizEnum;
import com.abc.itbbs.order.domain.enums.OrderTypeEnum;
import com.abc.itbbs.order.domain.vo.PaymentVO;
import com.abc.itbbs.order.service.OrderService;
import com.abc.itbbs.order.service.PaymentService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayConfig alipayConfig;

    @Override
    public PaymentVO getOrderPaymentInfo(String orderSn) {
        Order order = orderService.selectOrderByOrderSn(orderSn);

        return buildPaymentVoByOrder(order);
    }

    private PaymentVO buildPaymentVoByOrder(Order order) {
        PaymentVO paymentVO = new PaymentVO();

        // 保留两位小数点，向上取值
        BigDecimal payAmount = order.getTotalAmount().setScale(2, BigDecimal.ROUND_UP);
        paymentVO.setTotal_amount(payAmount.toString());
        paymentVO.setOut_trade_no(order.getOrderSn());
        paymentVO.setSubject(
                String.format("%s-%s购买订单",
                        OrderTypeEnum.getDescByType(order.getOrderType()),
                        OrderBizEnum.getDescByBiz(order.getBiz())
                )
        );
        paymentVO.setBody(StringUtils.EMPTY);

        return paymentVO;
    }

    @Override
    public Boolean rsaCheck(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        return AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipay_public_key(),
                alipayConfig.getCharset(), alipayConfig.getSign_type());
    }

    @Override
    public String paymentCallback(PaymentAsyncDTO paymentAsyncDTO) {
        // todo
        // try {
        //     // 保存支付记录
        //     PaymentInfoEntity paymentInfo = buildPauymentInfo(payAsyncVo);
        //     paymentInfoService.save(paymentInfo);
        //
        //     // 更新订单状态
        //     orderService.update(new LambdaUpdateWrapper<OrderEntity>()
        //             .eq(OrderEntity::getOrderSn, payAsyncVo.getOut_trade_no())
        //             .eq(OrderEntity::getStatus, OrderStatusEnum.CREATE_NEW.getCode())
        //             .set(OrderEntity::getStatus, OrderStatusEnum.PAYED.getCode())
        //     );
        //     return "success";
        // } catch (Exception e) {
        //     log.error("回调出错：{}", e.getMessage(), e);
        //     return "error";
        // }

        return null;
    }

}
