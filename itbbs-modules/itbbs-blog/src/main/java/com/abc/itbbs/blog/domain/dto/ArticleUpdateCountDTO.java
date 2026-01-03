package com.abc.itbbs.blog.domain.dto;

import com.abc.itbbs.blog.domain.vo.ArticleMetaVO;
import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/1/3
 */
@Data
public class ArticleUpdateCountDTO extends ArticleMetaVO {

    private Long articleId;

}
