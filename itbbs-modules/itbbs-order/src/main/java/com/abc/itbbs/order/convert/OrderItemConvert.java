package com.abc.itbbs.order.convert;

import cn.hutool.core.bean.BeanUtil;
com.abc.itbbs.common.core.util.IdUtils;
import com.abc.common.util.SecurityUtils;
import com.abc.itbbs.order.domain.dto.OrderItemDTO;
import com.abc.itbbs.order.domain.entity.OrderItem;

/**
 * 订单明细转换器
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
public class OrderItemConvert {
    public static OrderItem buildDefaultOrderItemByOrderItemDTO(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = BeanUtil.copyProperties(orderItemDTO, OrderItem.class);
        orderItem.setOrderItemId(IdUtils.getId());
        orderItem.setInsertParams();

        return orderItem;
    }
}
