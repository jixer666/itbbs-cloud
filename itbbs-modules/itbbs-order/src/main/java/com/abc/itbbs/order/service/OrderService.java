package com.abc.itbbs.order.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.entity.Order;
import com.abc.itbbs.order.domain.vo.OrderVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 订单接口
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
public interface OrderService extends IService<Order> {

    PageResult getOrderPageWithUiParam(OrderDTO orderDTO);

    void updateOrder(OrderDTO orderDTO);

    Long saveOrder(OrderDTO orderDTO);

    void deleteOrder(OrderDTO orderDTO);

    OrderVO saveOrderConfirm(OrderDTO orderDTO);

    OrderVO getOrderInfo(Long orderId);

    Order selectOrderByOrderSn(String orderSn);
}
