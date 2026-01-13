package com.abc.itbbs.common.ai.strategy.chat;

import com.abc.itbbs.common.ai.listener.StreamResponseListener;
import com.abc.itbbs.common.ai.model.AiMessageResponse;
import com.abc.itbbs.common.ai.model.Prompt;

public interface ChatStrategy {

    AiMessageResponse chat(Prompt prompt);

    void chatStream(Prompt prompt, StreamResponseListener streamResponseListener);

}
