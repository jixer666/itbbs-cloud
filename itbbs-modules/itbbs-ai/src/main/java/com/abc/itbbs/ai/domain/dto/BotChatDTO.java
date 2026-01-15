package com.abc.itbbs.ai.domain.dto;

import com.abc.itbbs.common.ai.filter.PromptInjectionFilter;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Data
public class BotChatDTO {

    private Long sessionId;

    private String content;

    private Integer mode;

    public void checkParams() {
        AssertUtils.isNotEmpty(this, "参数不能为空");
        AssertUtils.isNotEmpty(content, "内容不能为空");

        // 清理并校验注入
        String cleanContent = PromptInjectionFilter.sanitizeInput(content);
        if (!content.equalsIgnoreCase(cleanContent)) {
            content = cleanContent;
        }
    }
}
