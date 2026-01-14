package com.abc.itbbs.ai.controller;

import com.abc.itbbs.ai.domain.dto.ChatSessionDTO;
import com.abc.itbbs.ai.service.ChatSessionService;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天会话控制器
 *
 * @author LiJunXi
 * @date 2026-01-14
 */
@Api(tags = "聊天会话接口")
@RestController
@RequestMapping("/ai/chatSession")
public class ChatSessionController {

    @Autowired
    private ChatSessionService chatSessionService;

    @ApiOperation("查询聊天会话分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getChatSessionPage(ChatSessionDTO chatSessionDTO) {
        PageResult chatSessionPages = chatSessionService.getChatSessionPageWithUiParam(chatSessionDTO);

        return ApiResult.success(chatSessionPages);
    }

    @ApiOperation("更新聊天会话")
    @PutMapping
    public ApiResult<Void> updateChatSession(@RequestBody ChatSessionDTO chatSessionDTO) {
        chatSessionService.updateChatSession(chatSessionDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增聊天会话")
    @PostMapping
    public ApiResult<Void> saveChatSession(@RequestBody ChatSessionDTO chatSessionDTO) {
        chatSessionService.saveChatSession(chatSessionDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除聊天会话")
    @DeleteMapping
    public ApiResult<Void> deleteChatSession(@RequestBody ChatSessionDTO chatSessionDTO) {
        chatSessionService.deleteChatSession(chatSessionDTO);

        return ApiResult.success();
    }

}
