package com.abc.itbbs.blog.mapper;

import com.abc.itbbs.blog.domain.dto.ArticleHisDTO;
import com.abc.itbbs.blog.domain.entity.ArticleHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文章历史修订Mapper接口
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Mapper
public interface ArticleHisMapper extends BaseMapper<ArticleHis> {
    List<ArticleHis> selectArticleHisList(ArticleHisDTO articleHisDTO);
}
