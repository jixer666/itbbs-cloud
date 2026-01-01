package com.abc.itbbs.blog.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.blog.domain.enums.ArticleStatusEnum;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.common.core.util.JsoupUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;

import java.util.Date;

/**
 * 文章转换器
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
public class ArticleConvert {
    public static Article buildDefaultArticleByArticleDTO(ArticleDTO articleDTO) {
        Article article = BeanUtil.copyProperties(articleDTO, Article.class);
        article.setArticleId(IdUtils.getId());
        article.setUserId(SecurityUtils.getUserId());
        article.setTagDetails(JSONUtil.toJsonStr(article.getTagDetails()));
        article.setInsertParams();
        article.setStatus(ArticleStatusEnum.PENDING.getStatus());

        if (StringUtils.isEmpty(articleDTO.getSummary())) {
            String parseContent = JsoupUtils.parseHtml(article.getContent());
            String summary = StringUtils.truncateText(parseContent, 200);
            article.setSummary(summary);
        }

        return article;
    }

    public static Article buildDefaultArticleDraft() {
        Article article = new Article();
        article.setArticleId(IdUtils.getId());
        article.setUserId(SecurityUtils.getUserId());
        article.setStatus(ArticleStatusEnum.DRAFT.getStatus());
        article.setLikeCount(CommonConstants.ZERO);
        article.setViewsCount(CommonConstants.ZERO);
        article.setCollectCount(CommonConstants.ZERO);
        article.setInsertParams();

        return article;
    }

    public static void fillArticleUpdateParams(Article article, ArticleDTO articleDTO) {
        article.setTagDetails(JSONUtil.toJsonStr(articleDTO.getTagDetailsList()));
        if (StringUtils.isEmpty(articleDTO.getSummary())) {
            String parseContent = JsoupUtils.parseHtml(articleDTO.getContent());
            String summary = StringUtils.truncateText(parseContent, 200);
            article.setSummary(summary);
        }
        article.setUpdateParams();
    }
}
