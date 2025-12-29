package com.abc.itbbs.blog.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 分类DTO对象
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
public class CategoryDTO {

    private Long categoryId;

    private String categoryName;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> categoryIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "分类参数不能为空");
        AssertUtils.isNotEmpty(categoryId, "分类ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "分类参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "分类参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(categoryIds), "分类ID列表不能为空");
    }
}
