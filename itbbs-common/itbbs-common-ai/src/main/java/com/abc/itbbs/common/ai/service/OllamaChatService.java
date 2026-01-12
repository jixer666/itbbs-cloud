package com.abc.itbbs.common.ai.service;

import com.abc.itbbs.common.ai.constant.AiConstants;

public class OllamaChatService extends BaseChatService {

    @Override
    protected String getRequestUrl() {
        return AiConstants.OLLAMA_ENDPOINT + AiConstants.OLLAMA_REQUEST_PATH;
    }

    @Override
    protected String getModel() {
        return AiConstants.OLLAMA_MODEL;
    }

    @Override
    protected String getApiKey() {
        return "";
    }
}
