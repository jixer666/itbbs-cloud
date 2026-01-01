package com.abc.itbbs.blog.listener;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.constant.TemplateConstants;
import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.blog.service.SensitiveWordService;
import com.abc.itbbs.common.core.constant.FileSuffixConstants;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.abc.itbbs.common.mq.producer.RabbitMQProducer;
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
@RabbitListener(queues = RabbitMQConstants.BLOG_ARTICLE_CREATE_QUEUE)
public class ArticleCreatedEventListener {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    /**
     * 文章发布事件监听
     */
    @RabbitHandler
    public void handleStockLockedRelease(Article article, Message message, Channel channel) throws IOException {
        log.info("===开始消费文章发布===");
        try {
            // 审核文件内容
            Boolean result = sensitiveWordService.handleArticleContent(article.getArticleId(), article.getContent());
            if (!result) {
                // 审核未通过
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }

            // 审核通过
            // 生成静态文件
            rabbitMQProducer.sendMessage(RabbitMQConstants.BLOG_ARTICLE_EXCHANGE, RabbitMQConstants.BLOG_ARTICLE_HTML_KEY, message);

            // 保存ES
//            rabbitMQProducer.sendMessage(RabbitMQConstants.BLOG_ARTICLE_EXCHANGE, RabbitMQConstants.BLOG_ARTICLE_ES_KEY, message);
//
//            // 保存向量数据库
//            rabbitMQProducer.sendMessage(RabbitMQConstants.BLOG_ARTICLE_EXCHANGE, RabbitMQConstants.BLOG_ARTICLE_VECTOR_KEY, message);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
        log.info("===完成消费文章发布===");
    }

}
