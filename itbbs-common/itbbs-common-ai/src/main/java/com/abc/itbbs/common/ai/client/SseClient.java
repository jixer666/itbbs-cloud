package com.abc.itbbs.common.ai.client;

import com.abc.itbbs.common.ai.listener.StreamResponseListener;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

public class SseClient extends EventSourceListener {

    private StreamResponseListener listener;

    public SseClient(StreamResponseListener listener) {
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
            System.out.println("收到数据: " + data);
            // TODO
//             listener.onMessage();
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
