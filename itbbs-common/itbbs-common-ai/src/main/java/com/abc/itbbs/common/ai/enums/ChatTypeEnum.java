package com.abc.itbbs.common.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ChatTypeEnum {

    DEEPSEEK(1, "deepSeekChatStrategy"),
    OLLAMA(2, "ollamaChatStrategy");

    private Integer type;
    private String clazz;


    public static Integer getTypeByClass(String clazz) {
        return Arrays.stream(ChatTypeEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(ChatTypeEnum::getType).findFirst().orElse(null);
    }
}
