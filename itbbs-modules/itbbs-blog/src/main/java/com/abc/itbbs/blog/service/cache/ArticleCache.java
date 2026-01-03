package com.abc.itbbs.blog.service.cache;

import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.blog.mapper.ArticleMapper;
import com.abc.itbbs.common.redis.cache.AbstractRedisStringCache;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LiJunXi
 * @date 2026/1/3
 */
@Component
public class ArticleCache extends AbstractRedisStringCache<Long, Article> {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    protected String getKey(Long id) {
        return CacheConstants.getFinalKey(CacheConstants.ARTICLE_INFO, id);
    }

    @Override
    protected Map<Long, Article> load(List<Long> ids) {
        List<Article> articles = articleMapper.selectByIds(ids);
        return articles.stream().collect(Collectors.toMap(Article::getArticleId, Function.identity()));
    }

    @Override
    protected Long getExpireSeconds() {
        return CacheConstants.ARTICLE_INFO_EXPIRE_TIME;
    }
}
