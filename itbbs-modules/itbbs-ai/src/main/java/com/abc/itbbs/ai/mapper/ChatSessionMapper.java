package com.abc.itbbs.ai.mapper;

import com.abc.itbbs.ai.domain.dto.ChatSessionDTO;
import com.abc.itbbs.ai.domain.entity.ChatSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 聊天会话Mapper接口
 *
 * @author LiJunXi
 * @date 2026-01-14
 */
@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {
    List<ChatSession> selectChatSessionList(ChatSessionDTO chatSessionDTO);
}
