package com.abc.itbbs.ai.strategy.chatmode;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.common.ai.enums.AiMessageRoleEnum;
import com.abc.itbbs.common.ai.model.AiMessage;
import com.abc.itbbs.common.ai.model.Prompt;

public interface ChatModeStrategy {

    Prompt getPrompt(BotChatDTO chatDTO);

    default AiMessage buildUserAiMessage(String content) {
        AiMessage aiMessage = new AiMessage();
        aiMessage.setContent(content);
        aiMessage.setRole(AiMessageRoleEnum.USER.getRole());

        return aiMessage;
    }

}
