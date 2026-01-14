package com.abc.itbbs.ai.controller;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.ai.domain.vo.BotChatVO;
import com.abc.itbbs.ai.service.BotService;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author LiJunXi
 * @date 2026/1/11
 */
@Api(tags = "机器人接口")
@RestController
@RequestMapping("/ai/bot")
public class BotController {

    @Autowired
    private BotService botService;

    @ApiOperation("AI聊天")
    @PostMapping("/chat")
    public ApiResult<BotChatVO> chat(@RequestBody BotChatDTO botChatDTO) {
        BotChatVO botChatVO = botService.chat(botChatDTO);

        return ApiResult.success(botChatVO);
    }


    @ApiOperation("AI聊天Stream流")
    @PostMapping("/chat/stream")
    public SseEmitter chatStream(@RequestBody BotChatDTO botChatDTO) {

        return botService.chatStream(botChatDTO);
    }

}
