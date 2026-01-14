package com.abc.itbbs.ai.service.impl;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.ai.domain.vo.BotChatVO;
import com.abc.itbbs.ai.factory.ChatModeStrategyFactory;
import com.abc.itbbs.ai.service.BotService;
import com.abc.itbbs.ai.strategy.chatmode.ChatModeStrategy;
import com.abc.itbbs.common.ai.ChatSseEmitter;
import com.abc.itbbs.common.ai.factory.ChatStrategyFactory;
import com.abc.itbbs.common.ai.listener.StreamResponseListener;
import com.abc.itbbs.common.ai.model.AiMessage;
import com.abc.itbbs.common.ai.model.AiMessageResponse;
import com.abc.itbbs.common.ai.model.AiMessageStreamResponse;
import com.abc.itbbs.common.ai.model.Prompt;
import com.abc.itbbs.common.ai.strategy.chat.ChatStrategy;
import com.abc.itbbs.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Slf4j
@Service
public class BotServiceImpl implements BotService {

    @Value("${ai.type}")
    private Integer aiType;

    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public BotChatVO chat(BotChatDTO botChatDTO) {
        botChatDTO.checkParams();

        Prompt prompt = buildPrompt(botChatDTO);
        ChatStrategy chatStrategy = getChatService();
        AiMessageResponse aiMessageResponse = chatStrategy.chat(prompt);

        return buildAiChatVo(aiMessageResponse);
    }

    private ChatStrategy getChatService() {
        return ChatStrategyFactory.getChatStrategy(aiType);
    }

    @Override
    public SseEmitter chatStream(BotChatDTO botChatDTO) {
        botChatDTO.checkParams();

        ChatSseEmitter chatSseEmitter = createSseEmitter(botChatDTO);

        chatStream(botChatDTO, chatSseEmitter);

        return chatSseEmitter;
    }

    private ChatSseEmitter createSseEmitter(BotChatDTO botChatDTO) {
        ChatSseEmitter chatSseEmitter = ChatSseEmitter.create();

        String emitterKey = botChatDTO.getSessionId() + "-" + SecurityUtils.getUserId();

        chatSseEmitter.onCompletion(() -> {
            emitters.remove(emitterKey);
            log.debug("SSE连接完成，移除用户[{}]的Emitter", emitterKey);
        });
        emitters.put(emitterKey, chatSseEmitter);
        log.debug("新增SSE连接，用户[{}]，当前活跃连接数：{}", emitterKey, emitters.size());

        return chatSseEmitter;
    }

    private void chatStream(BotChatDTO botChatDTO,  ChatSseEmitter chatSseEmitter) {
        Prompt prompt = buildPrompt(botChatDTO);

        ChatStrategy chatStrategy = getChatService();
        chatStrategy.chatStream(prompt, new StreamResponseListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onMessage(AiMessageStreamResponse response) {
                try {
                    AiMessage aiMessage = response.getChoices().get(0).getDelta();
                    chatSseEmitter.send(aiMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStop() {
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (throwable != null && throwable.getCause() instanceof ClientAbortException) {
                    chatSseEmitter.complete();
                }
            }
        });
    }

    private BotChatVO buildAiChatVo(AiMessageResponse aiMessageResponse) {
        BotChatVO botChatVO = new BotChatVO();
        botChatVO.setContent(aiMessageResponse.getMessage().getContent());

        return botChatVO;
    }

    private Prompt buildPrompt(BotChatDTO botChatDTO) {
        ChatModeStrategy chatModeStrategy = ChatModeStrategyFactory.getChatModeStrategy(botChatDTO.getMode());
        return chatModeStrategy.getPrompt(botChatDTO);
    }

}
