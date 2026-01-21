package com.abc.itbbs.order.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单明细DTO对象
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
public class OrderItemDTO {

    private Long orderItemId;

    private Long orderId;

    private String orderSn;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer productQuantity;

    private BigDecimal promotionAmount;


    // 用于批量删除
    private List<Long> orderItemIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "订单明细参数不能为空");
        AssertUtils.isNotEmpty(orderItemId, "订单明细ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "订单明细参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "订单明细参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(orderItemIds), "订单明细ID列表不能为空");
    }
}
