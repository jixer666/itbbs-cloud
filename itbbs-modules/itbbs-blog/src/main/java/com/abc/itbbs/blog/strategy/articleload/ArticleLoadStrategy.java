package com.abc.itbbs.blog.strategy.articleload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.dto.ArticlePreloadDTO;
import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.common.redis.util.RedisUtils;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public interface ArticleLoadStrategy {

    Integer getLoadType();

    String getArticleLoadCacheKey();

    Long getArticleLoadExpireTime();

    List<ArticleVO> getArticleListByPreloadDTO(ArticlePreloadDTO articlePreloadDTO);

    default List<ArticleVO> getArticleList(ArticleDTO articleDTO) {
        AssertUtils.isTrue(getLoadType().equals(articleDTO.getLoadType()), "文章加载类型不匹配");

        String articleNewListCacheKey = getArticleLoadCacheKey();

        Long pageBegin = (articleDTO.getPageNum() - 1) * articleDTO.getPageSize();
        Long pageNum = articleDTO.getPageSize();

        Set<String> articleVoSet = RedisUtils.zRange(articleNewListCacheKey, pageBegin, pageNum);
        return articleVoSet.stream().map(item -> JSONUtil.toBean(item, ArticleVO.class)).collect(Collectors.toList());
    }

    default void saveArticleCache(ArticlePreloadDTO articlePreloadDTO) {
        List<ArticleVO> articleVOList = getArticleListByPreloadDTO(articlePreloadDTO);
        if (CollUtil.isEmpty(articleVOList)) {
            return;
        }

        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        for (ArticleVO articleVO : articleVOList) {
            tuples.add(new DefaultTypedTuple<>(JSONUtil.toJsonStr(articleVO), articleVO.getCreateTime().getTime() * -1.0));
        }

        String articleNewListCacheKey = getArticleLoadCacheKey();
        RedisUtils.zAdd(articleNewListCacheKey, tuples);
        RedisUtils.expire(articleNewListCacheKey, getArticleLoadExpireTime(), TimeUnit.HOURS);
    }
}
