package com.abc.itbbs.order.domain.entity;

import com.abc.itbbs.common.core.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单退款申请实体
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
@Builder
@TableName("tb_order_return_apply")
@AllArgsConstructor
@NoArgsConstructor
public class OrderReturnApply extends BaseEntity {

    @TableId
    @ApiModelProperty("订单退款申请ID")
    private Long orderReturnApplyId;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("退款理由")
    private String reaseon;

    @ApiModelProperty("商品总额")
    private BigDecimal productAmount;

    @ApiModelProperty("订单明细")
    private String productDetails;


}
