package com.abc.itbbs.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;
import com.abc.itbbs.order.constant.OrderConstants;
import com.abc.itbbs.order.convert.OrderConvert;
import com.abc.itbbs.order.convert.OrderItemConvert;
import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.dto.OrderItemDTO;
import com.abc.itbbs.order.domain.entity.Order;
import com.abc.itbbs.order.domain.entity.OrderItem;
import com.abc.itbbs.order.domain.enums.OrderBizEnum;
import com.abc.itbbs.order.domain.vo.OrderVO;
import com.abc.itbbs.order.factory.OrderBizStrategyFactory;
import com.abc.itbbs.order.mapper.OrderMapper;
import com.abc.itbbs.order.service.OrderItemService;
import com.abc.itbbs.order.service.OrderService;
import com.abc.itbbs.order.strategy.order.OrderBizStrategy;
import com.abc.itbbs.order.util.OrderUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单业务处理
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public PageResult getOrderPageWithUiParam(OrderDTO orderDTO) {
        startPage();
        List<Order> orders = orderMapper.selectOrderList(orderDTO);
        List<OrderVO> orderVOList = pageList2CustomList(orders, (List<Order> list) -> {
            return BeanUtil.copyToList(list, OrderVO.class);
        });

        return buildPageResult(orderVOList);
    }

    @Override
    public void updateOrder(OrderDTO orderDTO) {
        orderDTO.checkUpdateParams();
        Order order = orderMapper.selectById(orderDTO.getOrderId());
        AssertUtils.isNotEmpty(order, "订单不存在");
        BeanUtils.copyProperties(orderDTO, order);
        orderMapper.updateById(order);
    }

    @Override
    public Long saveOrder(OrderDTO orderDTO) {
        orderDTO.checkSaveParams();
        // 检查确认订单并获取缓存参数
        OrderDTO confirmOrder = checkConfirmParamsAndGetCache(orderDTO);

        // 构建提交订单
        Order order = buildSaveOrderByConfirmCache(confirmOrder);
        // 构建提交订单明细
        List<OrderItem> orderItemList = buildSaveOrderItemListByConfirmCache(order, orderDTO.getOrderItemList());

        transactionTemplate.execute(item -> {
            orderMapper.insert(order);
            orderItemService.saveOrderItemBatch(orderItemList);

            return null;
        });

        return order.getOrderId();
    }

    private List<OrderItem> buildSaveOrderItemListByConfirmCache(Order order, List<OrderItemDTO> orderItemList) {
        return orderItemList.stream().map(item -> {
            OrderItem orderItem = OrderItemConvert.buildDefaultOrderItemByOrderItemDTO(item);
            orderItem.setOrderId(order.getOrderId());
            orderItem.setOrderSn(order.getOrderSn());
            orderItem.setGrowth(item.getPromotionAmount().intValue());
            return orderItem;
        }).collect(Collectors.toList());
    }

    private OrderDTO checkConfirmParamsAndGetCache(OrderDTO orderDTO) {
        // 检查订单有效期
        String confirmUuid = orderDTO.getConfirmUuid();
        String orderConfirmStr = RedisUtils.get(CacheConstants.getFinalKey(CacheConstants.ORDER_CONFIRM_KEY, confirmUuid));
        AssertUtils.isTrue(StringUtils.isNotEmpty(orderConfirmStr), "确认订单已失效，请重新确认");

        // 防重检查
        Long userId = SecurityUtils.getUserId();
        Boolean isRepeatSubmit = RedisUtils.cad(CacheConstants.getFinalKey(CacheConstants.ORDER_CONFIRM_UUID_KEY, userId), userId.toString());
        AssertUtils.isTrue(isRepeatSubmit, "请勿重复提交");

        return JSONUtil.toBean(orderConfirmStr, OrderDTO.class);
    }

    private Order buildSaveOrderByConfirmCache(OrderDTO confirmOrder) {
        OrderBizStrategy orderBizStrategy = OrderBizStrategyFactory.getOrderBizStrategy(confirmOrder.getBiz());

        Order order = OrderConvert.buildDefaultOrderByOrderDTO(confirmOrder);
        orderBizStrategy.fillOrderSubmitParams(order, confirmOrder.getOrderItemList());

        return order;
    }

    @Override
    public void deleteOrder(OrderDTO orderDTO) {
        orderDTO.checkDeleteParams();

        orderMapper.deleteBatchIds(orderDTO.getOrderIds());
    }

    @Override
    public OrderVO saveOrderConfirm(OrderDTO orderDTO) {
        // 初步检查
        orderDTO.checkConfirmParams();

        OrderBizStrategy orderBizStrategy = OrderBizStrategyFactory.getOrderBizStrategy(orderDTO.getBiz());
        // 检查并赋值
        orderBizStrategy.checkAndFillCreateParams(orderDTO);

        // 计算商品价格
        calculateProductPrice(orderDTO);

        // 保存确认订单缓存
        saveOrderConfirmCache(orderDTO);

        return OrderConvert.buildOrderVOByOrderDTO(orderDTO);
    }

    private void saveOrderConfirmCache(OrderDTO orderDTO) {
        // 缓存确认订单信息
        orderDTO.setConfirmUuid(OrderUtils.getOrderSn(OrderBizEnum.ARTICLE.getBiz()));
        RedisUtils.set(CacheConstants.getFinalKey(CacheConstants.ORDER_CONFIRM_KEY, orderDTO.getConfirmUuid()),
                JSONUtil.toJsonStr(orderDTO),
                CacheConstants.ORDER_CONFIRM_UUID_EXPIRE_TIME, TimeUnit.MINUTES);

        // 防止重复提交
        Long userId = SecurityUtils.getUserId();
        RedisUtils.set(CacheConstants.getFinalKey(CacheConstants.ORDER_CONFIRM_UUID_KEY, userId),
                userId,
                CacheConstants.ORDER_CONFIRM_UUID_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    private void calculateProductPrice(OrderDTO orderDTO) {
        if (CollUtil.isEmpty(orderDTO.getOrderItemList())) {
            return;
        }

        BigDecimal totalAmount = new BigDecimal(OrderConstants.DEFAULT_PRICE);
        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItemList()) {
            BigDecimal promotionAmount = orderItemDTO.getProductPrice().multiply(new BigDecimal(orderItemDTO.getProductQuantity().toString()));
            orderItemDTO.setPromotionAmount(promotionAmount);

            totalAmount = totalAmount.add(promotionAmount);
        }
        orderDTO.setTotalAmount(totalAmount);
    }

    @Override
    public OrderVO getOrderInfo(Long orderId) {
        AssertUtils.isNotEmpty(orderId, "订单ID不能为空");

        // todo 获取订单信息

        return null;
    }
}