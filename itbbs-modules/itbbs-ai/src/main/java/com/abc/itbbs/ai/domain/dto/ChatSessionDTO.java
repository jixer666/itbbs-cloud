package com.abc.itbbs.ai.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 聊天会话DTO对象
 *
 * @author LiJunXi
 * @date 2026-01-14
 */
@Data
public class ChatSessionDTO {

    private Long sessionId;

    private String sessionName;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> chatSessionIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "聊天会话参数不能为空");
        AssertUtils.isNotEmpty(sessionId, "聊天会话ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "聊天会话参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "聊天会话参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(chatSessionIds), "聊天会话ID列表不能为空");
    }
}
