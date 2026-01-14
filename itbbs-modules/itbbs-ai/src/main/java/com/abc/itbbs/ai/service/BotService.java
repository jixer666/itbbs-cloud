package com.abc.itbbs.ai.service;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.ai.domain.vo.BotChatVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
public interface BotService {

    BotChatVO chat(BotChatDTO botChatDTO);

    SseEmitter chatStream(BotChatDTO botChatDTO);
}
