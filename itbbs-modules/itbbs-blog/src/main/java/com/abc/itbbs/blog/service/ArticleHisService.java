package com.abc.itbbs.blog.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.ArticleHisDTO;
import com.abc.itbbs.blog.domain.entity.ArticleHis;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文章历史修订接口
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
public interface ArticleHisService extends IService<ArticleHis> {

    PageResult getArticleHisPageWithUiParam(ArticleHisDTO articleHisDTO);

    void updateArticleHis(ArticleHisDTO articleHisDTO);

    void saveArticleHis(ArticleHisDTO articleHisDTO);

    void deleteArticleHis(ArticleHisDTO articleHisDTO);
}
