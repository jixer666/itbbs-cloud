package com.abc.itbbs.ai.service.impl;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.ai.domain.enums.BotChatStepEnum;
import com.abc.itbbs.ai.domain.vo.BotChatVO;
import com.abc.itbbs.ai.factory.ChatModeStrategyFactory;
import com.abc.itbbs.ai.service.BotService;
import com.abc.itbbs.ai.strategy.chatmode.ChatModeStrategy;
import com.abc.itbbs.common.ai.ChatSseEmitter;
import com.abc.itbbs.common.ai.factory.ChatStrategyFactory;
import com.abc.itbbs.common.ai.filter.AiOutputFilter;
import com.abc.itbbs.common.ai.listener.StreamResponseListener;
import com.abc.itbbs.common.ai.model.AiMessage;
import com.abc.itbbs.common.ai.model.AiMessageResponse;
import com.abc.itbbs.common.ai.model.AiMessageStreamResponse;
import com.abc.itbbs.common.ai.model.Prompt;
import com.abc.itbbs.common.ai.strategy.chat.ChatStrategy;
import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Slf4j
@Service
public class BotServiceImpl implements BotService {

    @Value("${ai.type}")
    private Integer aiType;

    @Autowired
    @Qualifier(value = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

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
        sendMessage(chatSseEmitter, BotChatStepEnum.START, null);

        threadPoolTaskExecutor.execute(() -> {
            chatStream(botChatDTO, chatSseEmitter);
        });

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
        sendMessage(chatSseEmitter, BotChatStepEnum.FIRST, null);
        Prompt prompt = buildPrompt(botChatDTO);

        ChatStrategy chatStrategy = getChatService();
        chatStrategy.chatStream(prompt, new StreamResponseListener() {
            @Override
            public void onStart() {
                try {
                    sendMessage(chatSseEmitter, BotChatStepEnum.SECOND, null);
                } catch (Exception e) {
                    log.error("连接ESS出错：{}", e.getMessage(), e);
                }
            }

            @Override
            public void onMessage(AiMessageStreamResponse response) {
                try {
                    AiMessage aiMessage = response.getChoices().get(0).getDelta();
                    checkOutputContent(aiMessage);

                    sendMessage(chatSseEmitter, BotChatStepEnum.SECOND, aiMessage);
                } catch (Exception e) {
                    log.error("推送消息出错：{}", e.getMessage(), e);
                }
            }

            @Override
            public void onStop() {
                try {
                    sendMessage(chatSseEmitter, BotChatStepEnum.THIRD, null);

                    // 参考内容
                     onReference();
                } catch (Exception e) {
                    log.error("关闭ESS出错：{}", e.getMessage(), e);
                }
            }

            private void onReference() {
                try {
                    List<Object> referenceList = buildReference(prompt, botChatDTO);
                    sendMessage(chatSseEmitter, BotChatStepEnum.FOURTH, referenceList);


                    sendMessage(chatSseEmitter, BotChatStepEnum.END, null);
                } catch (Exception e) {
                    log.error("推送参考内容出错：{}", e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (throwable != null && throwable.getCause() instanceof ClientAbortException) {
                    chatSseEmitter.complete();
                }
            }
        });
    }

    private void checkOutputContent(AiMessage aiMessage) {
        if (Objects.isNull(aiMessage)) {
            return;
        }
        if (StringUtils.isEmpty(aiMessage.getContent())) {
            return;
        }

        AiOutputFilter.ValidationResult validationResult = AiOutputFilter.validateOutput(aiMessage.getContent());
        if (validationResult.isValid) {
            return;
        }

        log.info("AI返回内容校验失败，原内容{}改为{}", aiMessage.getContent(), validationResult.filteredContent);
        aiMessage.setContent(validationResult.filteredContent);
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

    private List<Object> buildReference(Prompt prompt, BotChatDTO botChatDTO) {
        ChatModeStrategy chatModeStrategy = ChatModeStrategyFactory.getChatModeStrategy(botChatDTO.getMode());
        return chatModeStrategy.getReference(prompt);
    }

    private BotChatVO buildAiChatVo(Integer step, Object data) {
        BotChatVO botChatVO = new BotChatVO();
        botChatVO.setStep(step);
        botChatVO.setData(data);

        return botChatVO;
    }

    private void sendMessage(SseEmitter sseEmitter, BotChatStepEnum botChatStepEnum, Object data) {
        try {
            BotChatVO botChatVO = buildAiChatVo(botChatStepEnum.getStep(), data);
            sseEmitter.send(botChatVO);
        } catch (Exception e) {
            log.error("聊天对话{}步骤出错：{}", botChatStepEnum.getDesc(), e.getMessage(), e);
        }
    }
}
