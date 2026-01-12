package com.abc.itbbs.common.ai.service;

import com.abc.itbbs.common.ai.listener.StreamResponseListener;
import com.abc.itbbs.common.ai.model.AiMessageResponse;
import com.abc.itbbs.common.ai.model.ChatOptions;
import com.abc.itbbs.common.ai.model.Prompt;

public interface ChatService {

    AiMessageResponse chat(Prompt prompt);

    void chatStream(Prompt prompt, StreamResponseListener streamResponseListener);

}
