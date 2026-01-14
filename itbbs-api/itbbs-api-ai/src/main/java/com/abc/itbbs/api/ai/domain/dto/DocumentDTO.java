package com.abc.itbbs.api.ai.domain.dto;

import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

@Data
public class DocumentDTO {

    private Integer biz;

    private Object obj;

    public void checkVectorSaveParams() {
        AssertUtils.isNotEmpty(biz, "文档业务类型不能为空");
    }
}
