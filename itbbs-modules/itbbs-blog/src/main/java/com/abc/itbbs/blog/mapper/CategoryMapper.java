package com.abc.itbbs.blog.mapper;

import com.abc.itbbs.blog.domain.dto.CategoryDTO;
import com.abc.itbbs.blog.domain.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 分类Mapper接口
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> selectCategoryList(CategoryDTO categoryDTO);
}
