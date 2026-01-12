package com.abc.itbbs.ai.service;

import com.abc.itbbs.ai.domain.dto.AiChatDTO;
import com.abc.itbbs.ai.domain.vo.AiChatVO;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
public interface AiService {

    AiChatVO chatAi(AiChatDTO aiChatDTO);

}
