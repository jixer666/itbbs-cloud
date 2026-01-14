package com.abc.itbbs.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.ai.convert.ChatSessionConvert;
import com.abc.itbbs.ai.domain.dto.ChatSessionDTO;
import com.abc.itbbs.ai.domain.entity.ChatSession;
import com.abc.itbbs.ai.domain.vo.ChatSessionVO;
import com.abc.itbbs.ai.mapper.ChatSessionMapper;
import com.abc.itbbs.ai.service.ChatSessionService;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天会话业务处理
 *
 * @author LiJunXi
 * @date 2026-01-14
 */
@Service
public class ChatSessionServiceImpl extends BaseServiceImpl<ChatSessionMapper, ChatSession> implements ChatSessionService {

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Override
    public PageResult getChatSessionPageWithUiParam(ChatSessionDTO chatSessionDTO) {
        startPage();
        List<ChatSession> chatSessions = chatSessionMapper.selectChatSessionList(chatSessionDTO);
        List<ChatSessionVO> chatSessionVOList = pageList2CustomList(chatSessions, (List<ChatSession> list) -> {
            return BeanUtil.copyToList(list, ChatSessionVO.class);
        });

        return buildPageResult(chatSessionVOList);
    }

    @Override
    public void updateChatSession(ChatSessionDTO chatSessionDTO) {
        chatSessionDTO.checkUpdateParams();
        ChatSession chatSession = chatSessionMapper.selectById(chatSessionDTO.getSessionId());
        AssertUtils.isNotEmpty(chatSession, "聊天会话不存在");
        BeanUtils.copyProperties(chatSessionDTO, chatSession);
        chatSessionMapper.updateById(chatSession);
    }

    @Override
    public void saveChatSession(ChatSessionDTO chatSessionDTO) {
        chatSessionDTO.checkSaveParams();
        ChatSession chatSession = ChatSessionConvert.buildDefaultChatSessionByChatSessionDTO(chatSessionDTO);
        chatSessionMapper.insert(chatSession);
    }

    @Override
    public void deleteChatSession(ChatSessionDTO chatSessionDTO) {
        chatSessionDTO.checkDeleteParams();

        chatSessionMapper.deleteBatchIds(chatSessionDTO.getChatSessionIds());
    }
    

}