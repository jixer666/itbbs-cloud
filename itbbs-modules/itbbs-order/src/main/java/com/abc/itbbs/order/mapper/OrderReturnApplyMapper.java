package com.abc.itbbs.order.mapper;

import com.abc.itbbs.order.domain.dto.OrderReturnApplyDTO;
import com.abc.itbbs.order.domain.entity.OrderReturnApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单退款申请Mapper接口
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Mapper
public interface OrderReturnApplyMapper extends BaseMapper<OrderReturnApply> {
    List<OrderReturnApply> selectOrderReturnApplyList(OrderReturnApplyDTO orderReturnApplyDTO);
}
