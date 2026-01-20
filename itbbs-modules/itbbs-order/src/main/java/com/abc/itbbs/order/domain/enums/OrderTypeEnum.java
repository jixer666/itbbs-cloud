package com.abc.itbbs.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderTypeEnum {
    COMMON(1, "普通订单"),
    SECKILL(2, "秒杀订单");

    private Integer type;
    private String desc;

}
