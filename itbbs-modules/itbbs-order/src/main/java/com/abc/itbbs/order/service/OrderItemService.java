package com.abc.itbbs.order.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.order.domain.dto.OrderItemDTO;
import com.abc.itbbs.order.domain.entity.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 订单明细接口
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
public interface OrderItemService extends IService<OrderItem> {

    PageResult getOrderItemPageWithUiParam(OrderItemDTO orderItemDTO);

    void updateOrderItem(OrderItemDTO orderItemDTO);

    void saveOrderItem(OrderItemDTO orderItemDTO);

    void deleteOrderItem(OrderItemDTO orderItemDTO);

    void saveOrderItemBatch(List<OrderItem> orderItemList);
}
