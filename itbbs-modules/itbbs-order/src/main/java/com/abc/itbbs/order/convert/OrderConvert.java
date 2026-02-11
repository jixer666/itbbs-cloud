package com.abc.itbbs.order.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;
import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.entity.Order;
import com.abc.itbbs.order.domain.entity.OrderItem;
import com.abc.itbbs.order.domain.enums.OrderBizEnum;
import com.abc.itbbs.order.domain.vo.OrderItemVO;
import com.abc.itbbs.order.domain.vo.OrderVO;
import com.abc.itbbs.order.util.OrderUtils;

import java.util.List;

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
        order.setOrderSn(OrderUtils.getOrderSn(OrderBizEnum.ARTICLE.getBiz()));
        order.setUserId(SecurityUtils.getUserId());
        order.setInsertParams();

        return order;
    }

    public static OrderVO buildOrderVOByOrderDTO(OrderDTO orderDTO) {
        OrderVO orderVO = new OrderVO();
        orderVO.setBiz(orderDTO.getBiz());
        orderVO.setOrderType(orderDTO.getOrderType());
        orderVO.setConfirmUuid(orderDTO.getConfirmUuid());
        orderVO.setOrderItemList(BeanUtil.copyToList(orderDTO.getOrderItemList(), OrderItemVO.class));

        return orderVO;
    }

    public static OrderVO buildOrderVOByOrderAndItemList(Order order, List<OrderItem> orderItemList) {
        OrderVO orderVO = BeanUtil.copyProperties(order, OrderVO.class);
        List<OrderItemVO> orderItemVOList = BeanUtil.copyToList(orderItemList, OrderItemVO.class);
        orderVO.setOrderItemList(orderItemVOList);

        return orderVO;
    }
}
