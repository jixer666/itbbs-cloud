package com.abc.itbbs.api.ai.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocumentBizEnum {

    ARTICLE(1, "articleDocumentBizStrategy");

    private Integer biz;
    private String clazz;

    public static Integer getBizByClass(String clazz) {
        return Arrays.stream(DocumentBizEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(DocumentBizEnum::getBiz).findFirst().orElse(null);
    }
}
