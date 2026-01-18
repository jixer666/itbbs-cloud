package com.abc.itbbs.resource.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.resource.domain.dto.ResourceDTO;
import com.abc.itbbs.resource.domain.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 资源接口
 *
 * @author LiJunXi
 * @date 2026-01-18
 */
public interface ResourceService extends IService<Resource> {

    PageResult getResourcePageWithUiParam(ResourceDTO resourceDTO);

    void updateResource(ResourceDTO resourceDTO);

    void saveResource(ResourceDTO resourceDTO);

    void deleteResource(ResourceDTO resourceDTO);
}
