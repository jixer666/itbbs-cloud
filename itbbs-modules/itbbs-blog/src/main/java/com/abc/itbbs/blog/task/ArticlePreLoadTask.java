package com.abc.itbbs.blog.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.blog.convert.ArticleConvert;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.dto.ArticlePreloadDTO;
import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.blog.domain.enums.ArticleLoadTypeEnum;
import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.common.job.JobConstants;
import com.abc.itbbs.common.job.TaskHelper;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.abc.itbbs.common.mq.producer.RabbitMQProducer;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文章信息预加载定时任务
 *
 * @author LiJunXi
 * @date 2026/1/8
 */
@Slf4j
@Component
public class ArticlePreLoadTask {

    @Autowired
    private TaskHelper taskHelper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private RedissonClient redissonClient;

    // 每天执行一次
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void run() {
        RLock lock = redissonClient.getLock(CacheConstants.getFinalKey(CacheConstants.ARTICLE_PRELOAD_LOCK_KEY));
        try {
            boolean isLock = lock.tryLock(CacheConstants.ARTICLE_PRELOAD_LOCK_EXPIRE_TIME, TimeUnit.SECONDS);
            if (!isLock) {
                // 没有获取到锁就直接放弃
                return;
            }
            taskHelper.run(JobConstants.ARTICLE_PRELOAD_TASK_NAME, JobConstants.ARTICLE_PRELOAD_TASK_TARGET, task -> {
                // 加载最新的文章
                List<ArticleVO> preloadNewArticleList = getPreloadArticleList(ArticleLoadTypeEnum.NEW.getType());
                // 加载最热的文章
                List<ArticleVO> preloadHotArticleList = getPreloadArticleList(ArticleLoadTypeEnum.HOT.getType());

                rabbitMQProducer.sendMessage(
                        RabbitMQConstants.BLOG_ARTICLE_EXCHANGE,
                        RabbitMQConstants.BLOG_ARTICLE_PRELOAD_BATCH_KEY,
                        new ArticlePreloadDTO(preloadNewArticleList, preloadHotArticleList)
                );

                return task;
            }, task -> {
                return task;
            });
        } catch (Exception e) {
            log.error("文章信息预加载出错：{}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }

    }

    private List<ArticleVO> getPreloadArticleList(Integer type) {
        ArticleDTO articleDTO = ArticleConvert.buildArticleDTOByPreload();
        articleDTO.setLoadType(type);

        List<Article> articles = articleService.selectArticleList(articleDTO);
        if (CollUtil.isEmpty(articles)) {
            return new ArrayList<>();
        }

        return BeanUtil.copyToList(articles, ArticleVO.class);
    }

}
