package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.Map;

@Data
public class ChatRequest {

    private String requestUrl;

    private Map<String, String> headers;

    private ChatRequestBody chatRequestBody;

}
