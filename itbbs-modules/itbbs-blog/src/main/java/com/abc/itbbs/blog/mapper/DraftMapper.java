package com.abc.itbbs.blog.mapper;

import com.abc.itbbs.blog.domain.dto.DraftDTO;
import com.abc.itbbs.blog.domain.entity.Draft;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 草稿Mapper接口
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Mapper
public interface DraftMapper extends BaseMapper<Draft> {
    List<Draft> selectDraftList(DraftDTO draftDTO);
}
