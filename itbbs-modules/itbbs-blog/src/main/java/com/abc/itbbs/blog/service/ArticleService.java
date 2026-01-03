package com.abc.itbbs.blog.service;

import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.dto.ArticleUpdateCountDTO;
import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.blog.domain.vo.ArticleMetaVO;
import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 文章接口
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
public interface ArticleService extends IService<Article> {

    PageResult getArticlePageWithUiParam(ArticleDTO articleDTO);

    void updateArticle(ArticleDTO articleDTO);

    void deleteArticle(ArticleDTO articleDTO);

    ArticleVO saveArticleDraft();

    void updateStatus(Long articleId, Integer status);

    void updateHtmlFilePathByArticleId(Long articleId, String filePath);

    ArticleMetaVO getArticleMetaInfo(Long articleId);

    Integer getArticleViewsCount(Long articleId);

    void increaseArticleViewsCount(Long articleId);

    void updateArticleCountBath(List<ArticleUpdateCountDTO> articleUpdateCountDTOList);
}
