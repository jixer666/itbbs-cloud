package com.abc.itbbs.blog.strategy.articleload;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.dto.ArticlePreloadDTO;
import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.common.core.util.BeanUtils;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public interface ArticleLoadStrategy {

    Integer getLoadType();

    String getArticleLoadCacheKey();

    Long getArticleLoadExpireTime();

    List<ArticleVO> getArticleListByPreloadDTO(ArticlePreloadDTO articlePreloadDTO);

    default List<ArticleVO> getArticleList(ArticleDTO articleDTO) {
        AssertUtils.isTrue(getLoadType().equals(articleDTO.getLoadType()), "文章加载类型不匹配");

        String articleListCacheKey = getArticleLoadCacheKey();

        // 获取文章ID列表
        Long pageBegin = (articleDTO.getPageNum() - 1) * articleDTO.getPageSize();
        Long pageNum = articleDTO.getPageSize();
        Set<String> articleVoSet = RedisUtils.zRange(articleListCacheKey, pageBegin, pageBegin + pageNum - 1);

        List<String> articleCackeList = articleVoSet.stream().map(
                item -> CacheConstants.getFinalKey(CacheConstants.ARTICLE_HASH_INFO, Long.parseLong(item))
        ).collect(Collectors.toList());

        // 根据ID列表查询数据
        return RedisUtils.batchGetHash(articleCackeList, null).stream().map(item ->{
            return BeanUtil.copyProperties(item, ArticleVO.class);
        }).collect(Collectors.toList());
    }

    default void saveArticleCache(ArticlePreloadDTO articlePreloadDTO) {
        List<ArticleVO> articleVOList = getArticleListByPreloadDTO(articlePreloadDTO);
        if (CollUtil.isEmpty(articleVOList)) {
            return;
        }

        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        Map<String, Map<String, String>> articlesMap = new HashMap<>();
        for (ArticleVO articleVO : articleVOList) {
            tuples.add(new DefaultTypedTuple<>(articleVO.getArticleId().toString(), articleVO.getCreateTime().getTime() * -1.0));
            articlesMap.put(CacheConstants.getFinalKey(CacheConstants.ARTICLE_HASH_INFO, articleVO.getArticleId()), BeanUtils.convertToMap(articleVO));
        }

        String articleNewListCacheKey = getArticleLoadCacheKey();
        // 缓存文章ID列表（用于分页查询）
        RedisUtils.zAdd(articleNewListCacheKey, tuples);
        RedisUtils.expire(articleNewListCacheKey, getArticleLoadExpireTime(), TimeUnit.HOURS);

        // 缓存文章详情信息
        RedisUtils.batchHMSet(articlesMap, CacheConstants.ARTICLE_INFO_EXPIRE_TIME, TimeUnit.SECONDS, true);
    }

    default Long getArticleTotalSize() {
        return RedisUtils.zSize(getArticleLoadCacheKey());
    }
}
