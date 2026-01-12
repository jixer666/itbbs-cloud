package com.abc.itbbs.common.ai.service;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.common.ai.listener.StreamResponseListener;
import com.abc.itbbs.common.ai.model.*;
import com.abc.itbbs.common.ai.util.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseChatService implements ChatService {

    @Override
    public AiMessageResponse chat(Prompt prompt) {
        ChatRequest chatRequest = buildChatRequest(prompt, false);

        return doChat(chatRequest);
    }

    @Override
    public void chatStream(Prompt prompt, StreamResponseListener streamResponseListener) {
        ChatRequest chatRequest = buildChatRequest(prompt, true);

        doChatStream(chatRequest, streamResponseListener);
    }

    private AiMessageResponse doChat(ChatRequest chatRequest) {
        HttpResponse httpResponse = HttpUtils.doPost(chatRequest.getRequestUrl(),
                chatRequest.getHeaders(),
                chatRequest.getChatRequestBody(),
                20000);

        return JSONUtil.toBean(httpResponse.body(), AiMessageResponse.class);
    }

    private void doChatStream(ChatRequest chatRequest, StreamResponseListener streamResponseListener) {
        HttpUtils.doPostStream(chatRequest.getRequestUrl(),
                chatRequest.getHeaders(),
                chatRequest.getChatRequestBody(),
                30000,
                streamResponseListener);
    }

    private ChatRequest buildChatRequest(Prompt prompt, Boolean isStream) {
        ChatRequest chatRequest = new ChatRequest();

        buildRequestUrl(chatRequest);
        buildRequestHeader(chatRequest);
        buildRequestBody(chatRequest, prompt, isStream);

        return chatRequest;
    }

    private void buildRequestUrl(ChatRequest chatRequest) {
        chatRequest.setRequestUrl(getRequestUrl());
    }

    private void buildRequestHeader(ChatRequest chatRequest) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + getApiKey());

        chatRequest.setHeaders(headers);
    }

    private ChatRequest buildRequestBody(ChatRequest chatRequest, Prompt prompt, Boolean isStream) {
        ChatRequestBody chatRequestBody = new ChatRequestBody();
        chatRequestBody.setStream(isStream);
        chatRequestBody.setModel(getModel());
        chatRequestBody.setMessages(prompt.getMessages());

        chatRequest.setChatRequestBody(chatRequestBody);

        return chatRequest;
    }


    protected abstract String getRequestUrl();

    protected abstract String getModel();

    protected abstract String getApiKey();


}
