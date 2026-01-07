package com.abc.itbbs.blog.task;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.job.JobConstants;
import com.abc.itbbs.common.job.TaskHelper;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.abc.itbbs.common.mq.producer.RabbitMQProducer;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 文章浏览量、评论量和点赞量定时任务统计
 *
 * @author LiJunXi
 * @date 2026/1/3
 */
@Slf4j
@Component
public class ArticleCountTask {

    @Autowired
    private TaskHelper taskHelper;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Scheduled(fixedRate = 10 * 1000)
    public void run() {
        taskHelper.run(JobConstants.ARTICLE_COUNT_TASK_NAME, JobConstants.ARTICLE_COUNT_TASK_TARGET, task -> {
            // 注意：在多服务中可能重复取到数据，但对数据无影响，所以这里并没有做幂等性处理
            String articleCountWaitDoCacheKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_COUNT_WAIT_DO_TASK);
            Set<String> articleIdSet = RedisUtils.zRange(articleCountWaitDoCacheKey, 0, 999);
            if (CollUtil.isEmpty(articleIdSet)) {
                return null;
            }

            rabbitMQProducer.sendMessage(RabbitMQConstants.BLOG_ARTICLE_EXCHANGE, RabbitMQConstants.BLOG_ARTICLE_COUNT_KEY, articleIdSet);

            RedisUtils.zRemoveRange(articleCountWaitDoCacheKey, 0, articleIdSet.size() - 1);

            return task;
        }, task -> {
            return task;
        });
    }

}
