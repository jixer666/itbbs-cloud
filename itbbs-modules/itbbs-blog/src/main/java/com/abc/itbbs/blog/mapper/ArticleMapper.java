package com.abc.itbbs.blog.mapper;

import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}
