package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CollectTypeEnum {
    LIKE(1, "收藏"),
    UNLIKE(2, "取消收藏");

    private Integer type;
    private String desc;

}
