package com.abc.itbbs.ai.service.impl;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.ai.domain.vo.BotChatVO;
import com.abc.itbbs.ai.service.BotService;
import com.abc.itbbs.common.ai.enums.AiMessageRoleEnum;
import com.abc.itbbs.common.ai.factory.ChatStrategyFactory;
import com.abc.itbbs.common.ai.listener.StreamResponseListener;
import com.abc.itbbs.common.ai.model.AiMessage;
import com.abc.itbbs.common.ai.model.AiMessageResponse;
import com.abc.itbbs.common.ai.model.Prompt;
import com.abc.itbbs.common.ai.strategy.chat.ChatStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Service
public class BotServiceImpl implements BotService {

    @Value("${ai.type}")
    private Integer aiType;

    @Override
    public BotChatVO chat(BotChatDTO botChatDTO) {
        botChatDTO.checkParams();

        Prompt prompt = buildPrompt(botChatDTO.getContent());
        ChatStrategy chatStrategy = getChatService();
        AiMessageResponse aiMessageResponse = chatStrategy.chat(prompt);

        return buildAiChatVo(aiMessageResponse);
    }

    private ChatStrategy getChatService() {
        return ChatStrategyFactory.getChatStrategy(aiType);
    }

    @Override
    public void chatStream(BotChatDTO botChatDTO) {
        botChatDTO.checkParams();

        Prompt prompt = buildPrompt(botChatDTO.getContent());
        ChatStrategy chatStrategy = getChatService();
        chatStrategy.chatStream(prompt, new StreamResponseListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onMessage(AiMessageResponse response) {
                System.out.println("111");
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private BotChatVO buildAiChatVo(AiMessageResponse aiMessageResponse) {
        BotChatVO botChatVO = new BotChatVO();
        botChatVO.setContent(aiMessageResponse.getMessage().getContent());

        return botChatVO;
    }

    private Prompt buildPrompt(String content) {
        Prompt prompt = new Prompt();
        prompt.setMessages(Arrays.asList(buildUserAiMessage(content)));

        return prompt;
    }

    private AiMessage buildUserAiMessage(String content) {
        AiMessage aiMessage = new AiMessage();
        aiMessage.setContent(content);
        aiMessage.setRole(AiMessageRoleEnum.USER.getRole());

        return aiMessage;
    }

}
