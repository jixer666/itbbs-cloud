package com.abc.itbbs.common.ai.listener;

import com.abc.itbbs.common.ai.model.AiMessageStreamResponse;

public interface StreamResponseListener {

    void onStart();

    void onMessage(AiMessageStreamResponse response);

    void onStop();

    void onFailure(Throwable throwable);
}
