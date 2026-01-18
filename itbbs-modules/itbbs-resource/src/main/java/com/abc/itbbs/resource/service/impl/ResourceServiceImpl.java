package com.abc.itbbs.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.resource.convert.ResourceConvert;
import com.abc.itbbs.resource.domain.dto.ResourceDTO;
import com.abc.itbbs.resource.domain.entity.Resource;
import com.abc.itbbs.resource.domain.vo.ResourceVO;
import com.abc.itbbs.resource.mapper.ResourceMapper;
import com.abc.itbbs.resource.service.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源业务处理
 *
 * @author LiJunXi
 * @date 2026-01-18
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public PageResult getResourcePageWithUiParam(ResourceDTO resourceDTO) {
        startPage();
        List<Resource> resources = resourceMapper.selectResourceList(resourceDTO);
        List<ResourceVO> resourceVOList = pageList2CustomList(resources, (List<Resource> list) -> {
            return BeanUtil.copyToList(list, ResourceVO.class);
        });

        return buildPageResult(resourceVOList);
    }

    @Override
    public void updateResource(ResourceDTO resourceDTO) {
        resourceDTO.checkUpdateParams();
        Resource resource = resourceMapper.selectById(resourceDTO.getResourceId());
        AssertUtils.isNotEmpty(resource, "资源不存在");
        BeanUtils.copyProperties(resourceDTO, resource);
        resourceMapper.updateById(resource);
    }

    @Override
    public void saveResource(ResourceDTO resourceDTO) {
        resourceDTO.checkSaveParams();
        Resource resource = ResourceConvert.buildDefaultResourceByResourceDTO(resourceDTO);
        resourceMapper.insert(resource);
    }

    @Override
    public void deleteResource(ResourceDTO resourceDTO) {
        resourceDTO.checkDeleteParams();

        resourceMapper.deleteBatchIds(resourceDTO.getResourceIds());
    }
    

}