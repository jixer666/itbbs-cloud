package com.abc.itbbs.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrderBizEnum {
    ARTICLE(1, "文章", "articleOrderBizStrategy", 15);

    private Integer biz;
    private String desc;
    private String clazz;
    private Integer validTime;

    public static String getDescByBiz(Integer biz) {
        return Arrays.stream(OrderBizEnum.values()).filter(item -> item.getBiz().equals(biz)).map(OrderBizEnum::getDesc).findFirst().orElse(null);
    }

    public static Integer getTypeByClass(String clazz) {
        return Arrays.stream(OrderBizEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(OrderBizEnum::getBiz).findFirst().orElse(null);
    }
}
