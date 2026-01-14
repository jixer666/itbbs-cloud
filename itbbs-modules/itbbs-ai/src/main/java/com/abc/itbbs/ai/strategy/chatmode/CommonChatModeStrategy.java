package com.abc.itbbs.ai.strategy.chatmode;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.common.ai.model.Prompt;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CommonChatModeStrategy implements ChatModeStrategy {

    @Override
    public Prompt getPrompt(BotChatDTO chatDTO) {
        Prompt prompt = new Prompt();
        prompt.setMessages(Arrays.asList(buildUserAiMessage(chatDTO.getContent())));

        return prompt;
    }
}
