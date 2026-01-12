package com.abc.itbbs.ai.controller;

import com.abc.itbbs.ai.domain.dto.AiChatDTO;
import com.abc.itbbs.ai.domain.vo.AiChatVO;
import com.abc.itbbs.ai.service.AiService;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiJunXi
 * @date 2026/1/11
 */
@RestController
@RequestMapping("/ai/chat")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping
    public ApiResult<AiChatVO> chatAi(@RequestBody AiChatDTO aiChatDTO) {
        AiChatVO aiChatVO = aiService.chatAi(aiChatDTO);

        return ApiResult.success(aiChatVO);
    }

//    @PostMapping("/stream")
//    public ApiResult<Void> chatAiStream(@RequestBody AiChatDTO aiChatDTO) {
//        aiService.chatAiStream(aiChatDTO);
//
//        return ApiResult.success(aiChatVO);
//    }

}
