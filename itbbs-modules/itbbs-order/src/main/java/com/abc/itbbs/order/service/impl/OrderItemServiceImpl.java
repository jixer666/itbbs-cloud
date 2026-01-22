package com.abc.itbbs.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.order.convert.OrderItemConvert;
import com.abc.itbbs.order.domain.dto.OrderItemDTO;
import com.abc.itbbs.order.domain.entity.OrderItem;
import com.abc.itbbs.order.domain.vo.OrderItemVO;
import com.abc.itbbs.order.mapper.OrderItemMapper;
import com.abc.itbbs.order.service.OrderItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单明细业务处理
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public PageResult getOrderItemPageWithUiParam(OrderItemDTO orderItemDTO) {
        startPage();
        List<OrderItem> orderItems = orderItemMapper.selectOrderItemList(orderItemDTO);
        List<OrderItemVO> orderItemVOList = pageList2CustomList(orderItems, (List<OrderItem> list) -> {
            return BeanUtil.copyToList(list, OrderItemVO.class);
        });

        return buildPageResult(orderItemVOList);
    }

    @Override
    public void updateOrderItem(OrderItemDTO orderItemDTO) {
        orderItemDTO.checkUpdateParams();
        OrderItem orderItem = orderItemMapper.selectById(orderItemDTO.getOrderItemId());
        AssertUtils.isNotEmpty(orderItem, "订单明细不存在");
        BeanUtils.copyProperties(orderItemDTO, orderItem);
        orderItemMapper.updateById(orderItem);
    }

    @Override
    public void saveOrderItem(OrderItemDTO orderItemDTO) {
        orderItemDTO.checkSaveParams();
        OrderItem orderItem = OrderItemConvert.buildDefaultOrderItemByOrderItemDTO(orderItemDTO);
        orderItemMapper.insert(orderItem);
    }

    @Override
    public void saveOrderItemBatch(List<OrderItem> orderItemList) {
        if (CollUtil.isEmpty(orderItemList)) {
            return;
        }

        saveBatch(orderItemList);
    }

    @Override
    public void deleteOrderItem(OrderItemDTO orderItemDTO) {
        orderItemDTO.checkDeleteParams();

        orderItemMapper.deleteBatchIds(orderItemDTO.getOrderItemIds());
    }
    

}