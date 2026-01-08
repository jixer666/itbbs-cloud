package com.abc.itbbs.blog.domain.dto;

import com.abc.itbbs.blog.domain.vo.ArticleVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreloadDTO {

    private List<ArticleVO> newArticleList;

    private List<ArticleVO> hotArticleList;

}
