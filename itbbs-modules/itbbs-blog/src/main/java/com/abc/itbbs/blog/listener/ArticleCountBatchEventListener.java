package com.abc.itbbs.blog.listener;

import com.abc.itbbs.blog.convert.ArticleConvert;
import com.abc.itbbs.blog.domain.dto.ArticleUpdateCountDTO;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LiJunXi
 * @date 2026/1/1
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMQConstants.BLOG_ARTICLE_COUNT_QUEUE)
public class ArticleCountBatchEventListener {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章计数事件监听
     */
    @RabbitHandler
    public void handleArticleEsEvent(Set<String> articleIdSet, Message message, Channel channel) throws IOException {
        log.info("===开始消费文章计数批量构建===");
        try {
            List<ArticleUpdateCountDTO> articleUpdateCountDTOList = articleIdSet.stream()
                    .map(item -> {
                        Long articleId = Long.parseLong(item);
                        String viewsCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_VIEWS_COUNT, articleId);
                        String likeCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_VIEWS_COUNT, articleId);

                        Integer articleViewsCount = RedisUtils.get(viewsCountKey, Integer.class);
                        Integer articleLikeCount = RedisUtils.get(likeCountKey, Integer.class);

                        return ArticleConvert.buildArticleUpdateCountDTO(articleId, articleViewsCount, articleLikeCount, 0);
                    }).collect(Collectors.toList());

            articleService.updateArticleCountBath(articleUpdateCountDTOList);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
        log.info("===完成消费文章计数批量构建===");
    }

}
