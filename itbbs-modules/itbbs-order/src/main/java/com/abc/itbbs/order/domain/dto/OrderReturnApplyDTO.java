package com.abc.itbbs.order.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单退款申请DTO对象
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Data
public class OrderReturnApplyDTO {

    private Long orderReturnApplyId;

    private Long orderId;

    private String orderSn;

    private Long userId;

    private String reason;

    private BigDecimal productAmount;

    private String productDetails;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> orderReturnApplyIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "订单退款申请参数不能为空");
        AssertUtils.isNotEmpty(orderReturnApplyId, "订单退款申请ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "订单退款申请参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "订单退款申请参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(orderReturnApplyIds), "订单退款申请ID列表不能为空");
    }
}
