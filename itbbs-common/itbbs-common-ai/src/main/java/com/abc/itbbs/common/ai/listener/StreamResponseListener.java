package com.abc.itbbs.common.ai.listener;

import com.abc.itbbs.common.ai.model.AiMessageResponse;

public interface StreamResponseListener {

    void onStart();

    void onMessage(AiMessageResponse response);

    void onStop();

    void onFailure(Throwable throwable);
}
