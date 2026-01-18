package com.abc.itbbs.resource.mapper;

import com.abc.itbbs.resource.domain.dto.ResourceDTO;
import com.abc.itbbs.resource.domain.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 资源Mapper接口
 *
 * @author LiJunXi
 * @date 2026-01-18
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
    List<Resource> selectResourceList(ResourceDTO resourceDTO);
}
