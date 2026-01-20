package com.abc.itbbs.order.mapper;

import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    List<Order> selectOrderList(OrderDTO orderDTO);
}
