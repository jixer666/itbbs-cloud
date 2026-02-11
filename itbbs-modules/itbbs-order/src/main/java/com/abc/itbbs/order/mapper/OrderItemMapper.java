package com.abc.itbbs.order.mapper;

import com.abc.itbbs.order.domain.dto.OrderItemDTO;
import com.abc.itbbs.order.domain.entity.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单明细Mapper接口
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    List<OrderItem> selectOrderItemList(OrderItemDTO orderItemDTO);

    List<OrderItem> selectOrderItemListByOrderId(Long orderId);
}
