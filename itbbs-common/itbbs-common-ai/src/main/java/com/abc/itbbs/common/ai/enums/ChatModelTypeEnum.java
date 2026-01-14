package com.abc.itbbs.common.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ChatModelTypeEnum {

    DEEPSEEK(1, "deepSeekChatStrategy"),
    OLLAMA(2, "ollamaChatStrategy"),
    XUNFEI(3, "xunFeiChatStrategy");

    private Integer type;
    private String clazz;


    public static Integer getTypeByClass(String clazz) {
        return Arrays.stream(ChatModelTypeEnum.values()).filter(item -> item.getClazz().equals(clazz)).map(ChatModelTypeEnum::getType).findFirst().orElse(null);
    }
}
