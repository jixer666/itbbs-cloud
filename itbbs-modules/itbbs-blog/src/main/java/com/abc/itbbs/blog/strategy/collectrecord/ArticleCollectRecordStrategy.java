package com.abc.itbbs.blog.strategy.collectrecord;

import com.abc.itbbs.blog.domain.enums.CollectBizEnum;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ArticleCollectRecordStrategy implements CollectRecordStrategy {
    
    @Autowired
    private ArticleService articleService;

    @Override
    public Integer getType() {
        return CollectBizEnum.ARTICLE.getBiz();
    }

    @Override
    public String getCountCacheKey(Long targetId) {
        return CacheConstants.getFinalKey(CacheConstants.ARTICLE_COLLECT_COUNT, targetId);
    }

    @Override
    public String getCountLockCacheKey(Long targetId) {
        return CacheConstants.getFinalKey(CacheConstants.ARTICLE_COLLECT_COUNT_LOCK, targetId);
    }

    @Override
    public String getWaitDoTask() {
        return CacheConstants.getFinalKey(CacheConstants.ARTICLE_COUNT_WAIT_DO_TASK);
    }

    @Override
    public void saveCollectRecordCache(Long targetId) {
        Integer count = articleService.selectArticleCollectCount(targetId);
        RedisUtils.set(getCountCacheKey(targetId), count, CacheConstants.ARTICLE_COLLECT_COUNT_EXPIRE_TIME, TimeUnit.HOURS);
    }
}
