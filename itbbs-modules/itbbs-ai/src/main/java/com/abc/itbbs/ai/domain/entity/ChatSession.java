package com.abc.itbbs.ai.domain.entity;

import com.abc.itbbs.common.core.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聊天会话实体
 *
 * @author LiJunXi
 * @date 2026-01-14
 */
@Data
@Builder
@TableName("tb_chat_session")
@AllArgsConstructor
@NoArgsConstructor
public class ChatSession extends BaseEntity {

    @TableId
    @ApiModelProperty("会话ID")
    private Long sessionId;

    @ApiModelProperty("会话名称")
    private String sessionName;

    @ApiModelProperty("用户ID")
    private Long userId;


}
