package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CollectBizEnum {
    ARTICLE(1, "文章", "articleCollectRecordStrategy"),
    COMMENT(2, "评论", "");

    private Integer biz;
    private String desc;
    private String clazz;

    public static CollectBizEnum getByBiz(Integer biz) {
        return Arrays.stream(CollectBizEnum.values()).filter(item -> item.getBiz().equals(biz)).findFirst().orElse(null);
    }

    public static Integer getBizByClass(String clazz) {
        return Arrays.stream(CollectBizEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(CollectBizEnum::getBiz).findFirst().orElse(null);
    }
}
