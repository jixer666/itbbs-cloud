package com.abc.itbbs.common.ai.model;

import lombok.Data;

@Data
public class AiMessage {

    private String role;

    private String content;

    // 思考内容
    private String reasoning_content;

}
