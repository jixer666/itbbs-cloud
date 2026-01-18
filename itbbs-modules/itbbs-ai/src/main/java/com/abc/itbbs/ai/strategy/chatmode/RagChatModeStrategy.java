package com.abc.itbbs.ai.strategy.chatmode;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.ai.constant.BotConstants;
import com.abc.itbbs.ai.constant.DocumentConstants;
import com.abc.itbbs.ai.constant.document.ArticleDocumentEntityConstants;
import com.abc.itbbs.ai.domain.dto.BotChatDTO;
import com.abc.itbbs.ai.domain.entity.document.ArticleDocumentMilvusEntity;
import com.abc.itbbs.ai.domain.vo.ArticleDocumentReferenceVO;
import com.abc.itbbs.ai.util.MilvusUtils;
import com.abc.itbbs.api.blog.ArticleServiceClient;
import com.abc.itbbs.api.blog.domain.entity.Article;
import com.abc.itbbs.common.ai.factory.EmbeddingStrategyFactory;
import com.abc.itbbs.common.ai.model.Prompt;
import com.abc.itbbs.common.ai.strategy.embedding.EmbeddingStrategy;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import io.milvus.v2.service.vector.response.SearchResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RagChatModeStrategy implements ChatModeStrategy {

    @Value("${embedding.type}")
    private Integer embeddingType;

    @Value("${milvus.database}")
    private String database;

    @Autowired
    private ArticleServiceClient articleServiceClient;

    @Override
    public Prompt getPrompt(BotChatDTO chatDTO) {
        EmbeddingStrategy embeddingStrategy = EmbeddingStrategyFactory.getChatStrategy(embeddingType);
        List<Float> vectorList = embeddingStrategy.getVectorList(chatDTO.getContent());

        List<ArticleDocumentMilvusEntity> articleDocumentMilvusEntityList = matchAndGetArticleDocumentMilvusEntityList(vectorList);

        String content = buildPromptAiMessageUserContent(chatDTO.getContent(), articleDocumentMilvusEntityList);

        Prompt<ArticleDocumentMilvusEntity> prompt = new Prompt<>();
        prompt.setMessages(Arrays.asList(buildUserAiMessage(content)));
        prompt.setReferenceList(articleDocumentMilvusEntityList);

        return prompt;
    }

    private String buildPromptAiMessageUserContent(String question,
                                                   List<ArticleDocumentMilvusEntity> articleDocumentMilvusEntityList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < articleDocumentMilvusEntityList.size(); i++) {
            stringBuilder.append(i + 1).append(".").append(articleDocumentMilvusEntityList.get(i).getDocument_chunk());
        }

        return String.format(BotConstants.RAG_CHAT_PROMPT, stringBuilder.toString(), question);
    }

    private List<ArticleDocumentMilvusEntity> matchAndGetArticleDocumentMilvusEntityList(List<Float> vectorList) {
        SearchResp searchResp = MilvusUtils.searchSingle(database,
                DocumentConstants.DOCUMENT_CHUNK_COLLECTION,
                vectorList,
                ArticleDocumentEntityConstants.DOCUMENT_CHUNK_VECTOR,
                10,
                Arrays.asList(ArticleDocumentEntityConstants.ARTICLE_ID, ArticleDocumentEntityConstants.DOCUMENT_CHUNK));

        List<ArticleDocumentMilvusEntity> articleDocumentMilvusEntityList = new ArrayList<>();
        Set<Long> articleDocumentIdSet = new HashSet<>();
        for (List<SearchResp.SearchResult> searchResult : searchResp.getSearchResults()) {
            for (SearchResp.SearchResult result : searchResult) {
                if (result.getScore() < DocumentConstants.VECTOR_THRESHOLD) {
                    // 余弦相似度小于阈值直接抛弃
                    continue;
                }

                Long articleId = (Long) result.getEntity().get(ArticleDocumentEntityConstants.ARTICLE_ID);
                if (articleDocumentIdSet.contains(articleId)) {
                    // 去掉重复的文章，只保留一个优先级最大的文章即可
                    continue;
                }

                ArticleDocumentMilvusEntity articleDocumentMilvusEntity = new ArticleDocumentMilvusEntity();
                String documentChunk = result.getEntity().get(ArticleDocumentEntityConstants.DOCUMENT_CHUNK).toString();
                articleDocumentMilvusEntity.setArticle_id(articleId);
                articleDocumentMilvusEntity.setDocument_chunk(documentChunk);

                articleDocumentMilvusEntityList.add(articleDocumentMilvusEntity);
                articleDocumentIdSet.add(articleId);
            }
        }

        return articleDocumentMilvusEntityList.stream().limit(5).collect(Collectors.toList());
    }

    @Override
    public List<Object> getReference(Prompt prompt) {
        List<ArticleDocumentMilvusEntity> articleDocumentMilvusEntityList = BeanUtil.copyToList(prompt.getReferenceList(), ArticleDocumentMilvusEntity.class);

        Set<Long> articleIds = new HashSet<>();
        Map<Long, String> articleDocumentChunkMap = new HashMap<>();
        for (ArticleDocumentMilvusEntity articleDocumentMilvusEntity : articleDocumentMilvusEntityList) {
            articleDocumentChunkMap.put(articleDocumentMilvusEntity.getArticle_id(), articleDocumentMilvusEntity.getDocument_chunk());
            articleIds.add(articleDocumentMilvusEntity.getArticle_id());
        }

        Map<Long, Article> articleMap = ApiResult.invokeRemoteMethod(
                articleServiceClient.getArticleMapByIds(new ArrayList<>(articleIds))
        );

        return articleDocumentMilvusEntityList.stream().map(item -> {
            Article article = articleMap.get(item.getArticle_id());
            if (Objects.isNull(article)) {
                return new ArticleDocumentReferenceVO();
            }

            ArticleDocumentReferenceVO articleDocumentReferenceVO = new ArticleDocumentReferenceVO();
            articleDocumentReferenceVO.setArticleId(item.getArticle_id());
            articleDocumentReferenceVO.setDesc(item.getDocument_chunk());
            articleDocumentReferenceVO.setHtmlFilePath(article.getHtmlFilePath());
            articleDocumentReferenceVO.setTitle(article.getTitle());
            articleDocumentReferenceVO.setDesc(articleDocumentChunkMap.get(item.getArticle_id()));

            return articleDocumentReferenceVO;
        }).collect(Collectors.toList());
    }
}
