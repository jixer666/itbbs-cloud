package com.abc.itbbs.order.controller;

import com.abc.itbbs.order.config.AlipayConfig;
import com.abc.itbbs.order.constant.PaymentConstants;
import com.abc.itbbs.order.domain.dto.PaymentAsyncDTO;
import com.abc.itbbs.order.domain.vo.PaymentVO;
import com.abc.itbbs.order.service.PaymentService;
import com.alipay.api.AlipayApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "支付接口")
@Slf4j
@Controller
@RequestMapping("/order/payment")
public class PaymentController {

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private PaymentService paymentService;

    @ResponseBody
    @ApiOperation("调用支付宝支付")
    @GetMapping(value = "/aliPayOrder", produces = "text/html")
    public String aliPayOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PaymentVO paymentVO = paymentService.getOrderPaymentInfo(orderSn);

        return alipayConfig.pay(paymentVO);
    }

    @ApiOperation("支付回调")
    @GetMapping(value = "/callback")
    public String paymentCallback(PaymentAsyncDTO paymentAsyncDTO, HttpServletRequest request) {
        log.info("===============支付回调处理开始===============");
        // 验签
        Boolean checkResult = paymentService.rsaCheck(request);
        if (!checkResult) {
            log.info("验签失败");
            return PaymentConstants.SUCCESS;
        }
        log.info("验签成功");

        String result = paymentService.paymentCallback(paymentAsyncDTO);
        log.info("===============支付回调处理结束===============");

        return result;
    }


}
