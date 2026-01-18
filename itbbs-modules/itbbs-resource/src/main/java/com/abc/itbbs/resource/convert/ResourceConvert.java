package com.abc.itbbs.resource.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.resource.domain.dto.ResourceDTO;
import com.abc.itbbs.resource.domain.entity.Resource;

/**
 * 资源转换器
 *
 * @author LiJunXi
 * @date 2026-01-18
 */
public class ResourceConvert {
    public static Resource buildDefaultResourceByResourceDTO(ResourceDTO resourceDTO) {
        Resource resource = BeanUtil.copyProperties(resourceDTO, Resource.class);
        resource.setResourceId(IdUtils.getId());
        resource.setInsertParams();

        return resource;
    }
}
