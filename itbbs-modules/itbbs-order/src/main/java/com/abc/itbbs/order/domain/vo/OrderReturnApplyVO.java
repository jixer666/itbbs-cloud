package com.abc.itbbs.order.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 订单退款申请VO对象
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
public class OrderReturnApplyVO {

    private Long orderReturnApplyId;

    private Long orderId;

    private String orderSn;

    private Long userId;

    private String reaseon;

    private BigDecimal productAmount;

    private String productDetails;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
