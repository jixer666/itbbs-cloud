package com.abc.itbbs.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrderTypeEnum {
    COMMON(1, "普通订单"),
    SECKILL(2, "秒杀订单");

    private Integer type;
    private String desc;

    public static String getDescByType(Integer orderType) {
        return Arrays.stream(OrderTypeEnum.values()).filter(item -> item.getType().equals(orderType)).map(OrderTypeEnum::getDesc).findFirst().orElse(null);
    }
}
