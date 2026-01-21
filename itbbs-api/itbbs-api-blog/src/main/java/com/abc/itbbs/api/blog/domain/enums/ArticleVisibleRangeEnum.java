package com.abc.itbbs.api.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleVisibleRangeEnum {
    ALL(1, "全部可见"),
    ME(2, "仅我可见"),
    FANS(3, "粉丝可见"),
    VIP(4, "VIP可见"),
    CHARGE(5, "收费可见");

    private Integer visibleRange;
    private String desc;

    public static Boolean isChargeRange(Integer visibleRange) {
        return CHARGE.getVisibleRange().equals(visibleRange);
    }

}
