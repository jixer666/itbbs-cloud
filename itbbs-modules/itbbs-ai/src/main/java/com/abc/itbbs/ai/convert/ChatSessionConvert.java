package com.abc.itbbs.ai.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.ai.domain.dto.ChatSessionDTO;
import com.abc.itbbs.ai.domain.entity.ChatSession;
import com.abc.itbbs.common.core.util.IdUtils;

/**
 * 聊天会话转换器
 *
 * @author LiJunXi
 * @date 2026-01-14
 */
public class ChatSessionConvert {
    public static ChatSession buildDefaultChatSessionByChatSessionDTO(ChatSessionDTO chatSessionDTO) {
        ChatSession chatSession = BeanUtil.copyProperties(chatSessionDTO, ChatSession.class);
        chatSession.setSessionId(IdUtils.getId());
        chatSession.setInsertParams();

        return chatSession;
    }
}
