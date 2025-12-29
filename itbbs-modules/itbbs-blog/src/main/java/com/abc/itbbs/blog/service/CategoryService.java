package com.abc.itbbs.blog.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.CategoryDTO;
import com.abc.itbbs.blog.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 分类接口
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
public interface CategoryService extends IService<Category> {

    PageResult getCategoryPageWithUiParam(CategoryDTO categoryDTO);

    void updateCategory(CategoryDTO categoryDTO);

    void saveCategory(CategoryDTO categoryDTO);

    void deleteCategory(CategoryDTO categoryDTO);
}
