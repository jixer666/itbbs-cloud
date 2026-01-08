package com.abc.itbbs.blog.listener;

import com.abc.itbbs.blog.domain.dto.ArticlePreloadDTO;
import com.abc.itbbs.blog.service.ArticleService;
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
@RabbitListener(queues = RabbitMQConstants.BLOG_ARTICLE_PRELOAD_QUEUE)
public class ArticlePreloadEventListener {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章发布ES构建事件监听
     */
    @RabbitHandler
    public void handleArticlePreloadEvent(ArticlePreloadDTO articlePreloadDTO, Message message, Channel channel) throws IOException {
        log.info("===开始消费文章预加载构建===");
        try {
            articleService.loadArticleCache(articlePreloadDTO);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("文章预加载出错：{}", e.getMessage(), e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        log.info("===完成消费文章预加载构建===");
    }

}
