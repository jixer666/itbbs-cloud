package com.abc.itbbs.order.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.entity.Order;

/**
 * 订单转换器
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
public class OrderConvert {
    public static Order buildDefaultOrderByOrderDTO(OrderDTO orderDTO) {
        Order order = BeanUtil.copyProperties(orderDTO, Order.class);
        order.setOrderId(IdUtils.getId());
        order.setInsertParams();

        return order;
    }
}
