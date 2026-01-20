package com.abc.itbbs.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.order.convert.OrderConvert;
import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.entity.Order;
import com.abc.itbbs.order.domain.vo.OrderVO;
import com.abc.itbbs.order.mapper.OrderMapper;
import com.abc.itbbs.order.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void saveOrder(OrderDTO orderDTO) {
        orderDTO.checkSaveParams();
        Order order = OrderConvert.buildDefaultOrderByOrderDTO(orderDTO);
        orderMapper.insert(order);
    }

    @Override
    public void deleteOrder(OrderDTO orderDTO) {
        orderDTO.checkDeleteParams();

        orderMapper.deleteBatchIds(orderDTO.getOrderIds());
    }
    

}