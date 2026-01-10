package com.abc.itbbs.blog.listener;

import com.abc.itbbs.blog.constant.ArticleConstants;
import com.abc.itbbs.blog.convert.ArticleConvert;
import com.abc.itbbs.blog.domain.dto.ArticleUpdateCountDTO;
import com.abc.itbbs.blog.factory.ArticleLoadStrategyFactory;
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
import java.util.ArrayList;
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
    public void handleArticleCountEvent(Set<String> articleIdSet, Message message, Channel channel) throws IOException {
        log.info("===开始消费文章计数批量构建===");
        try {
            List<String> viewCountKeys = new ArrayList<>();
            List<Object> viewCountValues = new ArrayList<>();
            List<String> likeCountKeys = new ArrayList<>();
            List<Object> likeCountValues = new ArrayList<>();
            List<String> collectCountKeys = new ArrayList<>();
            List<Object> collectCountValues = new ArrayList<>();

            List<ArticleUpdateCountDTO> articleUpdateCountDTOList = articleIdSet.stream()
                    .map(item -> {
                        Long articleId = Long.parseLong(item);
                        String viewsCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_VIEWS_COUNT, articleId);
                        String likeCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_LIKE_COUNT, articleId);
                        String collectCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_COLLECT_COUNT, articleId);
                        String articleHashKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_HASH_INFO, articleId);

                        Integer articleViewsCount = RedisUtils.get(viewsCountKey, Integer.class);
                        Integer articleLikeCount = RedisUtils.get(likeCountKey, Integer.class);
                        Integer articleCollectCount = RedisUtils.get(collectCountKey, Integer.class);

                        viewCountKeys.add(articleHashKey);
                        likeCountKeys.add(articleHashKey);
                        collectCountKeys.add(articleHashKey);

                        viewCountValues.add(articleViewsCount);
                        likeCountValues.add(articleLikeCount);
                        collectCountValues.add(articleCollectCount);

                        return ArticleConvert.buildArticleUpdateCountDTO(articleId, articleViewsCount, articleLikeCount, articleCollectCount);
                    }).collect(Collectors.toList());

            // 更新数据库中的计数字段
            articleService.updateArticleCountBath(articleUpdateCountDTOList);

            // 更新Hash结构中的计数字段
            RedisUtils.batchUpdateHashField(viewCountKeys, ArticleConstants.ARTICLE_VIEW_FIELD, viewCountValues);
            RedisUtils.batchUpdateHashField(likeCountKeys, ArticleConstants.ARTICLE_LIKE_FIELD, likeCountValues);
            RedisUtils.batchUpdateHashField(collectCountKeys, ArticleConstants.ARTICLE_COLLECT_FIELD, collectCountValues);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
        log.info("===完成消费文章计数批量构建===");
    }

}
