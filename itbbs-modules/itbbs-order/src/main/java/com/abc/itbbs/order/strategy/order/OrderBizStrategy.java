package com.abc.itbbs.order.strategy.order;

import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.dto.OrderItemDTO;
import com.abc.itbbs.order.domain.entity.Order;

import java.util.List;

public interface OrderBizStrategy {

    void checkAndFillCreateParams(OrderDTO orderDTO);

    void fillOrderSubmitParams(Order order, List<OrderItemDTO> orderItemList);
}
