package com.abc.itbbs.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderBizEnum {
    ARTICLE(1, "文章");

    private Integer biz;
    private String desc;

}
