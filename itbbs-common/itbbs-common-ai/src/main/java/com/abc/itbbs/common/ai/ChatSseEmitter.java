package com.abc.itbbs.common.ai;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

/**
 *  聊天专用SSE发射器
 */
public class ChatSseEmitter extends SseEmitter {

    // 默认超时时间：30分钟
    public static final long DEFAULT_TIMEOUT = 5 * 60 * 1000L;

    public ChatSseEmitter() {
        super(DEFAULT_TIMEOUT);
        // 注册基础的超时/异常回调（避免连接泄漏）
        registerBasicCallbacks();
    }

    public ChatSseEmitter(long timeout) {
        super(timeout);
        registerBasicCallbacks();
    }

    /**
     * 基础回调：仅做简单的连接清理
     */
    private void registerBasicCallbacks() {
        // 超时回调
        onTimeout(this::complete);
        // 异常回调
        onError(e -> complete());
    }

    public static ChatSseEmitter create() {
        return new ChatSseEmitter();
    }

    /**
     * 发送普通消息（最常用的核心方法）
     * @param message 消息内容
     * @throws IOException 发送失败时抛出IO异常
     */
    public void sendMessage(String message) throws IOException {
        // 封装标准的SSE消息格式
        send(SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).name("message").data(message));
    }

    /**
     * 快速创建发射器
     */
    public ChatSseEmitter createChatSseEmitter() {
        return new ChatSseEmitter();
    }
}
