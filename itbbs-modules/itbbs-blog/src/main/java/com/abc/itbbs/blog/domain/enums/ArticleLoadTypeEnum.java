package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ArticleLoadTypeEnum {
    NEW(1, "最新", "newArticleLoadStrategy"),
    HOT(2, "热度", "hotArticleLoadStrategy");

    private Integer type;
    private String desc;
    private String clazz;

    public static Integer getTypeByClass(String clazz) {
        return Arrays.stream(ArticleLoadTypeEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(ArticleLoadTypeEnum::getType).findFirst().orElse(null);
    }
}
