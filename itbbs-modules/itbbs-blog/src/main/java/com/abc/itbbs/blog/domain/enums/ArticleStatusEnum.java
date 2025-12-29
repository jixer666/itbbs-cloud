package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {
    PENDING(1, "待审核"),
    PUBLISHED(2, "上架"),
    UN_PUBLISHED(3, "下架"),
    DELETE(4, "删除");

    private Integer status;
    private String desc;

}
