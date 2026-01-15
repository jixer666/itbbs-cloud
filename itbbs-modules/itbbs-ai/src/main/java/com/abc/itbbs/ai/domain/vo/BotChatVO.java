package com.abc.itbbs.ai.domain.vo;

import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Data
public class BotChatVO {

    // 普通输出
    private String content;

    // 流式输出
    private Integer step;

    private Object data;

}
