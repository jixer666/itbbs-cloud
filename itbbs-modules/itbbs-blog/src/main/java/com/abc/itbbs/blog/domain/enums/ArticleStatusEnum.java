package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {
    DRAFT(1, "草稿"),
    PENDING(2, "待审核"),
    PUBLISHED(3, "上架"),
    UN_PUBLISHED(4, "下架"),
    DELETE(5, "删除");

    private Integer status;
    private String desc;

}
