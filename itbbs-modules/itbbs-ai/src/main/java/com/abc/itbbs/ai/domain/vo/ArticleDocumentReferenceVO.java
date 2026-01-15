package com.abc.itbbs.ai.domain.vo;

import lombok.Data;

@Data
public class ArticleDocumentReferenceVO {

    private Long articleId;

    private String title;

    private String htmlFilePath;

    private String desc;

}
