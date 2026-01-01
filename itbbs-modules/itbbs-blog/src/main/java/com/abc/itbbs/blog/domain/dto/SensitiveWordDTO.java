package com.abc.itbbs.blog.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 敏感词DTO对象
 *
 * @author LiJunXi
 * @date 2025-12-31
 */
@Data
public class SensitiveWordDTO  {

    private Long wordId;

    private String word;

    private Integer level;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> sensitiveWordIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "敏感词参数不能为空");
        AssertUtils.isNotEmpty(wordId, "敏感词ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "敏感词参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "敏感词参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(sensitiveWordIds), "敏感词ID列表不能为空");
    }
}
