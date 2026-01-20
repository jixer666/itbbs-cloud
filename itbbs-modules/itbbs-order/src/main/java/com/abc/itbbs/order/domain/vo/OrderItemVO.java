package com.abc.itbbs.order.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 订单明细VO对象
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
public class OrderItemVO {

    private Long orderItemId;

    private Long orderId;

    private String orderSn;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer productQuantity;

    private BigDecimal promotionAmount;

    private Integer growth;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
