package com.abc.itbbs.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.order.convert.OrderReturnApplyConvert;
import com.abc.itbbs.order.domain.dto.OrderReturnApplyDTO;
import com.abc.itbbs.order.domain.entity.OrderReturnApply;
import com.abc.itbbs.order.domain.vo.OrderReturnApplyVO;
import com.abc.itbbs.order.mapper.OrderReturnApplyMapper;
import com.abc.itbbs.order.service.OrderReturnApplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单退款申请业务处理
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Service
public class OrderReturnApplyServiceImpl extends BaseServiceImpl<OrderReturnApplyMapper, OrderReturnApply> implements OrderReturnApplyService {

    @Autowired
    private OrderReturnApplyMapper orderReturnApplyMapper;

    @Override
    public PageResult getOrderReturnApplyPageWithUiParam(OrderReturnApplyDTO orderReturnApplyDTO) {
        startPage();
        List<OrderReturnApply> orderReturnApplys = orderReturnApplyMapper.selectOrderReturnApplyList(orderReturnApplyDTO);
        List<OrderReturnApplyVO> orderReturnApplyVOList = pageList2CustomList(orderReturnApplys, (List<OrderReturnApply> list) -> {
            return BeanUtil.copyToList(list, OrderReturnApplyVO.class);
        });

        return buildPageResult(orderReturnApplyVOList);
    }

    @Override
    public void updateOrderReturnApply(OrderReturnApplyDTO orderReturnApplyDTO) {
        orderReturnApplyDTO.checkUpdateParams();
        OrderReturnApply orderReturnApply = orderReturnApplyMapper.selectById(orderReturnApplyDTO.getOrderReturnApplyId());
        AssertUtils.isNotEmpty(orderReturnApply, "订单退款申请不存在");
        BeanUtils.copyProperties(orderReturnApplyDTO, orderReturnApply);
        orderReturnApplyMapper.updateById(orderReturnApply);
    }

    @Override
    public void saveOrderReturnApply(OrderReturnApplyDTO orderReturnApplyDTO) {
        orderReturnApplyDTO.checkSaveParams();
        OrderReturnApply orderReturnApply = OrderReturnApplyConvert.buildDefaultOrderReturnApplyByOrderReturnApplyDTO(orderReturnApplyDTO);
        orderReturnApplyMapper.insert(orderReturnApply);
    }

    @Override
    public void deleteOrderReturnApply(OrderReturnApplyDTO orderReturnApplyDTO) {
        orderReturnApplyDTO.checkDeleteParams();

        orderReturnApplyMapper.deleteBatchIds(orderReturnApplyDTO.getOrderReturnApplyIds());
    }
    

}