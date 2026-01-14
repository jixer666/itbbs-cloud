package com.abc.itbbs.ai.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 聊天会话VO对象
 *
 * @author LiJunXi
 * @date 2026-01-14
 */
@Data
public class ChatSessionVO {

    private Long sessionId;

    private String sessionName;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
