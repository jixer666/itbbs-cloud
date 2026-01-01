package com.abc.itbbs.blog.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.domain.dto.CategoryDTO;
import com.abc.itbbs.blog.domain.entity.Category;
import com.abc.itbbs.common.core.util.IdUtils;

/**
 * 分类转换器
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
public class CategoryConvert {
    public static Category buildDefaultCategoryByCategoryDTO(CategoryDTO categoryDTO) {
        Category category = BeanUtil.copyProperties(categoryDTO, Category.class);
        category.setCategoryId(IdUtils.getId());
        category.setInsertParams();

        return category;
    }
}
