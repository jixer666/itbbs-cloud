package com.abc.itbbs.blog.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 草稿DTO对象
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
public class DraftDTO {

    private Long draftId;

    private Long userId;

    private String title;

    private String content;

    private Long categoryId;

    private String tagDetails;

    private String cover;

    private Integer type;

    private Integer visibleRange;

    private Integer creationStatement;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> draftIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "草稿参数不能为空");
        AssertUtils.isNotEmpty(draftId, "草稿ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "草稿参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "草稿参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(draftIds), "草稿ID列表不能为空");
    }
}
