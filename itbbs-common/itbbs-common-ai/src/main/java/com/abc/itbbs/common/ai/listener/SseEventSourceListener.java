package com.abc.itbbs.common.ai.listener;

import cn.hutool.json.JSONUtil;
import com.abc.itbbs.common.ai.model.AiMessageResponse;
import com.abc.itbbs.common.ai.model.AiMessageStreamResponse;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

public class SseEventSourceListener extends EventSourceListener {

    private StreamResponseListener listener;

    public SseEventSourceListener(StreamResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        System.out.println("连接已打开");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (!"[DONE]".equals(data)) {
            // 解析并处理数据
            AiMessageStreamResponse response = JSONUtil.toBean(data, AiMessageStreamResponse.class);
            listener.onMessage(response);
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        System.out.println("连接已关闭");
    }

    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        System.err.println("发生错误: " + t.getMessage());
    }


}
