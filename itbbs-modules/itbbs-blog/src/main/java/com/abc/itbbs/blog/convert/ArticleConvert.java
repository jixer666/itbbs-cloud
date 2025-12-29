package com.abc.itbbs.blog.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.blog.domain.enums.ArticleStatusEnum;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.common.security.util.SecurityUtils;

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
        article.setCommonParams();
        article.setStatus(ArticleStatusEnum.PENDING.getStatus());

        return article;
    }
}
