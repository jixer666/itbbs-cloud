package com.abc.itbbs.api.ai.domain.dto;

import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

@Data
public class DocumentDTO {

    private String biz;

    private Long articleId;

    private String content;

    public void checkVectorSaveParams() {
        AssertUtils.isNotEmpty(articleId, "目标对象ID不能为空");
        AssertUtils.isNotEmpty(content, "内容不能为空");
    }
}
