package com.abc.itbbs.blog.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.domain.dto.ArticleHisDTO;
import com.abc.itbbs.blog.domain.entity.ArticleHis;
import com.abc.itbbs.common.core.util.IdUtils;

/**
 * 文章历史修订转换器
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
public class ArticleHisConvert {
    public static ArticleHis buildDefaultArticleHisByArticleHisDTO(ArticleHisDTO articleHisDTO) {
        ArticleHis articleHis = BeanUtil.copyProperties(articleHisDTO, ArticleHis.class);
        articleHis.setArticleHisId(IdUtils.getId());
        articleHis.setCommonParams();

        return articleHis;
    }
}
