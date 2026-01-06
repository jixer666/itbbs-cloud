package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LikeBizEnum {
    ARTICLE(1, "文章", "articleLikeRecordStrategy"),
    COMMENT(2, "评论", "");

    private Integer biz;
    private String desc;
    private String clazz;

    public static LikeBizEnum getByBiz(Integer biz) {
        return Arrays.stream(LikeBizEnum.values()).filter(item -> item.getBiz().equals(biz)).findFirst().orElse(null);
    }

    public static Integer getBizByClass(String clazz) {
        return Arrays.stream(LikeBizEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(LikeBizEnum::getBiz).findFirst().orElse(null);
    }
}
