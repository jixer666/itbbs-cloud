package com.abc.itbbs.common.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Getter
@AllArgsConstructor
public enum EmbeddingTypeEnum {

    OLLAMA(1, "ollamaEmbeddingStrategy");

    private Integer type;
    private String clazz;

    public static Integer getTypeByClass(String clazz) {
        return Arrays.stream(EmbeddingTypeEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(EmbeddingTypeEnum::getType).findFirst().orElse(null);
    }
}
