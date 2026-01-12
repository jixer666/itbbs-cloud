package com.abc.itbbs.ai.service.impl;

import com.abc.itbbs.ai.domain.dto.AiChatDTO;
import com.abc.itbbs.ai.domain.vo.AiChatVO;
import com.abc.itbbs.ai.service.AiService;
import com.abc.itbbs.common.ai.enums.AiMessageRoleEnum;
import com.abc.itbbs.common.ai.model.AiMessage;
import com.abc.itbbs.common.ai.model.AiMessageResponse;
import com.abc.itbbs.common.ai.model.Prompt;
import com.abc.itbbs.common.ai.service.ChatService;
import com.abc.itbbs.common.ai.service.OllamaChatService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Service
public class AiServiceImpl implements AiService {

    @Override
    public AiChatVO chatAi(AiChatDTO aiChatDTO) {
        aiChatDTO.checkParams();

        Prompt prompt = buildPrompt(aiChatDTO.getContent());
        ChatService chatService = new OllamaChatService();
        AiMessageResponse aiMessageResponse = chatService.chat(prompt);

        return buildAiChatVo(aiMessageResponse);
    }

    private AiChatVO buildAiChatVo(AiMessageResponse aiMessageResponse) {
        AiChatVO aiChatVO = new AiChatVO();
        aiChatVO.setContent(aiMessageResponse.getMessage().getContent());

        return aiChatVO;
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
