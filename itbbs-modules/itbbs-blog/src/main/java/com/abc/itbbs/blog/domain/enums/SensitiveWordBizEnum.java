package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SensitiveWordBizEnum {

    ARTICLE("article", "文章内容");

    private String biz;
    private String desc;


}
