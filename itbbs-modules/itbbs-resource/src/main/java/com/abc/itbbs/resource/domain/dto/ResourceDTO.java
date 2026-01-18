package com.abc.itbbs.resource.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 资源DTO对象
 *
 * @author LiJunXi
 * @date 2026-01-18
 */
@Data
public class ResourceDTO {

    private Long resourceId;

    private String resourceName;

    private Integer type;

    private Integer category;

    private String description;

    private Integer fileCount;

    private Long userId;

    private Integer point;

    private Integer downloadCount;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> resourceIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "资源参数不能为空");
        AssertUtils.isNotEmpty(resourceId, "资源ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "资源参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "资源参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(resourceIds), "资源ID列表不能为空");
    }
}
