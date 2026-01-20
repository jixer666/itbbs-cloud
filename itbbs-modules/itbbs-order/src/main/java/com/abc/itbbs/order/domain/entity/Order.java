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
import java.util.Date;

/**
 * 订单实体
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
@Builder
@TableName("tb_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

    @TableId
    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("业务类型")
    private Integer biz;

    @ApiModelProperty("订单类型")
    private Integer orderType;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("运费金额")
    private BigDecimal freightAmount;

    @ApiModelProperty("优惠卷金额")
    private BigDecimal couponAmount;

    @ApiModelProperty("积分抵扣金额")
    private BigDecimal integrationAmount;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("可获得成长值")
    private Integer growth;

    @ApiModelProperty("支付时间")
    private Date paymentTime;

    @ApiModelProperty("有效时间(分钟)")
    private Integer validTime;


}
