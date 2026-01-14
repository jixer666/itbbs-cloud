package com.abc.itbbs.api.ai.domain.dto;

import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/1/14
 */
@Data
public class ArticleDocumentDTO {

    private Long articleId;

    private String content;

    public void checkParams() {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isNotEmpty(content, "内容不能为空");
    }
}
