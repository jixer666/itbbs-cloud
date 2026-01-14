package com.abc.itbbs.ai.strategy.chatmode;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.common.ai.model.Prompt;
import org.springframework.stereotype.Service;

@Service
public class RagChatModeStrategy implements ChatModeStrategy {

    @Override
    public Prompt getPrompt(BotChatDTO chatDTO) {


        return null;
    }
}
