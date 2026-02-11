package com.abc.itbbs.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    COMMON(1, "普通订单"),
    SECKILL(2, "秒杀订单");

    private Integer status;
    private String desc;

}
