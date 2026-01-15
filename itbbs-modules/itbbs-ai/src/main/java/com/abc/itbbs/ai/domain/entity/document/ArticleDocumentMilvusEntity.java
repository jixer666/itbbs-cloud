package com.abc.itbbs.ai.domain.entity.document;

import lombok.Data;

import java.util.List;

@Data
public class ArticleDocumentMilvusEntity {

    private Long document_chunk_id;

    private Long article_id;

    private String document_chunk;

    private List<Float> document_chunk_vector;

    private Long created_at;

    private Long updated_at;

}
