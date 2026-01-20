package com.abc.itbbs.order.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.order.domain.dto.OrderReturnApplyDTO;
import com.abc.itbbs.order.domain.entity.OrderReturnApply;

/**
 * 订单退款申请转换器
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
public class OrderReturnApplyConvert {
    public static OrderReturnApply buildDefaultOrderReturnApplyByOrderReturnApplyDTO(OrderReturnApplyDTO orderReturnApplyDTO) {
        OrderReturnApply orderReturnApply = BeanUtil.copyProperties(orderReturnApplyDTO, OrderReturnApply.class);
        orderReturnApply.setOrderReturnApplyId(IdUtils.getId());
        orderReturnApply.setInsertParams();

        return orderReturnApply;
    }
}
