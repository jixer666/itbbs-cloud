package com.abc.itbbs.blog.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 点赞记录DTO对象
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
@Data
public class LikeRecordDTO {

    private Long likeRecordId;

    private Integer type;

    private Long targetId;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> likeRecordIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "点赞记录参数不能为空");
        AssertUtils.isNotEmpty(likeRecordId, "点赞记录ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "点赞记录参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "点赞记录参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(likeRecordIds), "点赞记录ID列表不能为空");
    }
}
