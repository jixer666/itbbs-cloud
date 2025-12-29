package com.abc.itbbs.blog.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 分类VO对象
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
public class CategoryVO {

    private Long categoryId;

    private String categoryName;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
