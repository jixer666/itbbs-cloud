package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.blog.convert.CategoryConvert;
import com.abc.itbbs.blog.domain.dto.CategoryDTO;
import com.abc.itbbs.blog.domain.entity.Category;
import com.abc.itbbs.blog.domain.vo.CategoryVO;
import com.abc.itbbs.blog.mapper.CategoryMapper;
import com.abc.itbbs.blog.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类业务处理
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageResult getCategoryPageWithUiParam(CategoryDTO categoryDTO) {
        startPage();
        List<Category> categorys = categoryMapper.selectCategoryList(categoryDTO);
        List<CategoryVO> categoryVOList = pageList2CustomList(categorys, (List<Category> list) -> {
            return BeanUtil.copyToList(list, CategoryVO.class);
        });

        return buildPageResult(categoryVOList);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        categoryDTO.checkUpdateParams();
        Category category = categoryMapper.selectById(categoryDTO.getCategoryId());
        AssertUtils.isNotEmpty(category, "分类不存在");
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateById(category);
    }

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        categoryDTO.checkSaveParams();
        Category category = CategoryConvert.buildDefaultCategoryByCategoryDTO(categoryDTO);
        categoryMapper.insert(category);
    }

    @Override
    public void deleteCategory(CategoryDTO categoryDTO) {
        categoryDTO.checkDeleteParams();

        categoryMapper.deleteBatchIds(categoryDTO.getCategoryIds());
    }
    

}