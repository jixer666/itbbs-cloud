package com.abc.itbbs.ai.controller;

import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.ai.domain.vo.BotChatVO;
import com.abc.itbbs.ai.service.BotService;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.exception.GlobalException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public SseEmitter chatStream(@RequestBody BotChatDTO botChatDTO,
                                 HttpServletResponse response) {
        forbiddenNginxFlux(response);

        return botService.chatStream(botChatDTO);
    }

    private void forbiddenNginxFlux(HttpServletResponse response) {
        response.setHeader("X-Accel-Buffering", "no"); // 禁用Nginx缓冲
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Content-Type", "text/event-stream; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // 立即刷新响应头
            response.flushBuffer();
        } catch (IOException e) {
            throw new GlobalException(BizCodeEnum.BIZ_ERROR.getCode(), "刷新响应头失败");
        }
    }

}
