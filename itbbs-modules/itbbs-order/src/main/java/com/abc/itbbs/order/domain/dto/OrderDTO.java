package com.abc.itbbs.order.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单DTO对象
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
public class OrderDTO {

    private Long orderId;

    private String orderSn;

    private Integer biz;

    private Integer orderType;

    private Long couponId;

    private Integer payType;

    private List<OrderItemDTO> orderItemList;

    private BigDecimal totalAmount;


    // 用于批量删除
    private List<Long> orderIds;

    // 确认订单UUID
    private String confirmUuid;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "订单参数不能为空");
        AssertUtils.isNotEmpty(orderId, "订单ID不能为空");
        AssertUtils.isNotEmpty(orderSn, "订单编号不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "订单参数不能为空");
        AssertUtils.isNotEmpty(biz, "订单业务不能为空");
        AssertUtils.isNotEmpty(orderType, "订单类型不能为空");
        AssertUtils.isNotEmpty(confirmUuid, "确认订单参数不能为空");
}

    public void checkConfirmParams() {
        AssertUtils.isNotEmpty(this, "订单参数不能为空");
        AssertUtils.isNotEmpty(biz, "订单业务不能为空");
        AssertUtils.isNotEmpty(orderType, "订单类型不能为空");
        AssertUtils.isFalse(CollUtil.isEmpty(orderItemList), "商品数量不足，无法创建");

        for (OrderItemDTO orderItemDTO : orderItemList) {
            AssertUtils.isTrue(orderItemDTO.getProductQuantity() > 0, "存在商品数量小于1，无法创建");
            AssertUtils.isTrue(orderItemDTO.getProductPrice().compareTo(new BigDecimal("0")) > 0, "商品价格小于0，无法创建");
        }
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "订单参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(orderIds), "订单ID列表不能为空");
    }
}
