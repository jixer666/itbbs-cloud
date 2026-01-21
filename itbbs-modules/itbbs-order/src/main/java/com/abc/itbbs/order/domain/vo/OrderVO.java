package com.abc.itbbs.order.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 订单VO对象
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
public class OrderVO {

    private Long orderId;

    private String orderSn;

    private Long userId;

    private Integer biz;

    private Integer orderType;

    private Long couponId;

    private BigDecimal totalAmount;

    private BigDecimal freightAmount;

    private BigDecimal couponAmount;

    private BigDecimal integrationAmount;

    private Integer payType;

    private Integer growth;

    private Date paymentTime;

    private Integer validTime;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;

    private List<OrderItemVO> orderItemList;

    // 确认订单UUID
    private String confirmUuid;

}
