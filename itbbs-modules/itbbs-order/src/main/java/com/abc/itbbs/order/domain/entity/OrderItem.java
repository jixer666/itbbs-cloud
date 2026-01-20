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
 * 订单明细实体
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
@Builder
@TableName("tb_order_item")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @TableId
    @ApiModelProperty("订单明细ID")
    private Long orderItemId;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品价格")
    private BigDecimal productPrice;

    @ApiModelProperty("商品数量")
    private Integer productQuantity;

    @ApiModelProperty("商品总额")
    private BigDecimal promotionAmount;

    @ApiModelProperty("可获得成长值")
    private Integer growth;


}
