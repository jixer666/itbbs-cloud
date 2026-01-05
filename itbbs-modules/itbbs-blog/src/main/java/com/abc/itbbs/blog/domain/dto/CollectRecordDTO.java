package com.abc.itbbs.blog.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 收藏记录DTO对象
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
@Data
public class CollectRecordDTO {

    private Long collectRecordId;

    private Integer type;

    private Long targetId;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> collectRecordIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "收藏记录参数不能为空");
        AssertUtils.isNotEmpty(collectRecordId, "收藏记录ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "收藏记录参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "收藏记录参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(collectRecordIds), "收藏记录ID列表不能为空");
    }
}
