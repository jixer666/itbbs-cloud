package com.abc.itbbs.order.strategy.order;

import com.abc.itbbs.order.domain.dto.OrderDTO;

public interface OrderBizStrategy {

    void checkAndFillCreateParams(OrderDTO orderDTO);
}
