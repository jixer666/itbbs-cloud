package com.abc.itbbs.ai.service;

import com.abc.itbbs.ai.domain.dto.ChatSessionDTO;
import com.abc.itbbs.ai.domain.entity.ChatSession;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 聊天会话接口
 *
 * @author LiJunXi
 * @date 2026-01-14
 */
public interface ChatSessionService extends IService<ChatSession> {

    PageResult getChatSessionPageWithUiParam(ChatSessionDTO chatSessionDTO);

    void updateChatSession(ChatSessionDTO chatSessionDTO);

    void saveChatSession(ChatSessionDTO chatSessionDTO);

    void deleteChatSession(ChatSessionDTO chatSessionDTO);
}
