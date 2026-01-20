package com.abc.itbbs.order.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.order.domain.dto.OrderReturnApplyDTO;
import com.abc.itbbs.order.domain.entity.OrderReturnApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 订单退款申请接口
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
public interface OrderReturnApplyService extends IService<OrderReturnApply> {

    PageResult getOrderReturnApplyPageWithUiParam(OrderReturnApplyDTO orderReturnApplyDTO);

    void updateOrderReturnApply(OrderReturnApplyDTO orderReturnApplyDTO);

    void saveOrderReturnApply(OrderReturnApplyDTO orderReturnApplyDTO);

    void deleteOrderReturnApply(OrderReturnApplyDTO orderReturnApplyDTO);
}
