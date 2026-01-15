package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.List;

@Data
public class Prompt<T> {

    // 提示词内容
    private List<AiMessage> messages;

    // 参考内容
    private List<T> referenceList;

}