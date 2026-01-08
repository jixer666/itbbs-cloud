package com.abc.itbbs.blog.strategy.articleload;

import com.abc.itbbs.blog.domain.dto.ArticlePreloadDTO;
import com.abc.itbbs.blog.domain.enums.ArticleLoadTypeEnum;
import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NewArticleLoadStrategy implements ArticleLoadStrategy {

    @Override
    public Integer getLoadType() {
        return ArticleLoadTypeEnum.NEW.getType();
    }

    @Override
    public String getArticleLoadCacheKey() {
        return CacheConstants.getFinalKey(CacheConstants.ARTICLE_HOT_LIST);
    }

    @Override
    public Long getArticleLoadExpireTime() {
        return CacheConstants.ARTICLE_NEW_LIST_EXPIRE_TIME;
    }

    @Override
    public List<ArticleVO> getArticleListByPreloadDTO(ArticlePreloadDTO articlePreloadDTO) {
        return articlePreloadDTO.getNewArticleList();
    }
}
