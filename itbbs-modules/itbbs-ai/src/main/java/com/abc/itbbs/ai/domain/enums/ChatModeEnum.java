package com.abc.itbbs.ai.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ChatModeEnum {

    COMMON(1, "commonChatModeStrategy"),
    RAG(2, "ragChatModeStrategy");

    private Integer type;
    private String clazz;

    public static Integer getTypeByClass(String clazz) {
        return Arrays.stream(ChatModeEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(ChatModeEnum::getType).findFirst().orElse(null);
    }
}
