package com.abc.itbbs.blog.listener;

import com.abc.itbbs.api.ai.DocumentServiceClient;
import com.abc.itbbs.api.ai.domain.dto.ArticleDocumentDTO;
import com.abc.itbbs.api.ai.domain.dto.DocumentDTO;
import com.abc.itbbs.api.ai.domain.enums.DocumentBizEnum;
import com.abc.itbbs.api.blog.domain.entity.Article;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author LiJunXi
 * @date 2026/1/1
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMQConstants.BLOG_ARTICLE_VECTOR_QUEUE)
public class ArticleCreateVectorEventListener {

    @Autowired
    private DocumentServiceClient documentServiceClient;

    /**
     * 文章发布向量数据库事件监听
     */
    @RabbitHandler
    public void handleArticleVectorEvent(Article article, Message message, Channel channel) throws IOException {
        log.info("===开始消费文章向量数据构建===");
        try {
            DocumentDTO documentDTO = buildDocumentDTO(article);
            documentServiceClient.vectorSave(documentDTO);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("文章向量数据构建出错：{}", e.getMessage(), e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        log.info("===完成消费文章向量数据构建===");
    }

    private DocumentDTO buildDocumentDTO(Article article) {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setBiz(DocumentBizEnum.ARTICLE.getBiz());

        ArticleDocumentDTO articleDocumentDTO = new ArticleDocumentDTO();
        articleDocumentDTO.setArticleId(article.getArticleId());
        articleDocumentDTO.setContent(article.getContent());
        documentDTO.setObj(articleDocumentDTO);

        return documentDTO;
    }

}
