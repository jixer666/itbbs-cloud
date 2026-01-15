package com.abc.itbbs.blog.mapper;

import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.dto.ArticleUpdateCountDTO;
import com.abc.itbbs.api.blog.domain.entity.Article;
import com.abc.itbbs.blog.domain.vo.ArticleMetaVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章Mapper接口
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Article> selectArticleList(ArticleDTO articleDTO);

    List<Article> selectArticleListWithoutContent(ArticleDTO articleDTO);

    ArticleMetaVO selectArticleMetaInfo(Long articleId);

    List<Article> selectByIds(@Param("list") List<Long> ids);

    Integer selectArticleViewsCount(Long articleId);

    void updateArticleCountBath(@Param("list") List<ArticleUpdateCountDTO> articleUpdateCountDTOList);

    Integer selectArticleLikeCount(Long articleId);

    Integer selectArticleCollectCount(Long articleId);
}
