package com.abc.itbbs.ai.domain.dto;

import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Data
public class AiChatDTO {

    private String content;

    public void checkParams() {
        AssertUtils.isNotEmpty(this, "参数不能为空");
        AssertUtils.isNotEmpty(content, "内容不能为空");
    }
}
