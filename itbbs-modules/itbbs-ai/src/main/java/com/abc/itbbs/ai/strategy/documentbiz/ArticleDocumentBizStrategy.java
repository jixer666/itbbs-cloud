package com.abc.itbbs.ai.strategy.documentbiz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.ai.constant.DocumentConstants;
import com.abc.itbbs.ai.domain.entity.document.ArticleDocumentMilvusEntity;
import com.abc.itbbs.api.ai.domain.dto.ArticleDocumentDTO;
import com.abc.itbbs.common.ai.factory.EmbeddingStrategyFactory;
import com.abc.itbbs.common.ai.model.DocumentChunk;
import com.abc.itbbs.common.ai.strategy.embedding.EmbeddingStrategy;
import com.abc.itbbs.common.ai.strategy.splitter.DocumentSplitter;
import com.abc.itbbs.common.ai.strategy.splitter.RecursiveDocumentSplitter;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LiJunXi
 * @date 2026/1/14
 */
@Service
public class ArticleDocumentBizStrategy implements DocumentBizStrategy<ArticleDocumentMilvusEntity> {

    @Value("${embedding.type}")
    private Integer embeddingType;

    @Override
    public List<ArticleDocumentMilvusEntity> run(Object object) {
        ArticleDocumentDTO articleDocumentDTO = BeanUtil.toBean(object, ArticleDocumentDTO.class);
        articleDocumentDTO.checkParams();

        String content = cleanArticleContentFormat(articleDocumentDTO.getContent());

        List<DocumentChunk> documentChunks = splitDocument(content);

        return getDocumentMilvusList(documentChunks, articleDocumentDTO.getArticleId());
    }

    private String cleanArticleContentFormat(String content) {
        if (StringUtils.isEmpty(content)) {
            return StringUtils.EMPTY;
        }

        String text = Jsoup.parse(content).text();

        return text.trim();
    }

    private List<ArticleDocumentMilvusEntity> getDocumentMilvusList(List<DocumentChunk> documentChunks, Long articleId) {
        if (CollUtil.isEmpty(documentChunks)) {
            return new ArrayList<>();
        }

        EmbeddingStrategy embeddingStrategy = EmbeddingStrategyFactory.getChatStrategy(embeddingType);

        return documentChunks.stream().map(item -> {
            ArticleDocumentMilvusEntity articleDocumentMilvusEntity = new ArticleDocumentMilvusEntity();
            articleDocumentMilvusEntity.setDocument_chunk_id(IdUtils.getId());
            articleDocumentMilvusEntity.setArticle_id(articleId);
            articleDocumentMilvusEntity.setDocument_chunk(item.getContent());

            List<Float> vectorList = embeddingStrategy.getVectorList(item.getContent());
            articleDocumentMilvusEntity.setDocument_chunk_vector(vectorList);

            Long now = new Date().getTime();
            articleDocumentMilvusEntity.setCreated_at(now);
            articleDocumentMilvusEntity.setUpdated_at(now);

            return articleDocumentMilvusEntity;
        }).collect(Collectors.toList());
    }

    private List<DocumentChunk> splitDocument(String content) {
        DocumentSplitter documentSplitter = new RecursiveDocumentSplitter(
                DocumentConstants.CHUNK_SIZE, DocumentConstants.CHUNK_OVER_LAP
        );
        return documentSplitter.split(content);
    }

}
