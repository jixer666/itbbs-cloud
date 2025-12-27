package com.abc.itbbs.common.oss.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.exception.GlobalException;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 文件DTO对象
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Data
public class FileDTO {

    private Long fileId;

    private String filename;

    private Long totalSize;

    private String fileType;

    private String fileMd5;

    private Integer ossType;

    private String filePath;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;

    // 用于批量删除
    private List<Long> fileIds;


    public void checkUpdateParams() {
        if (Objects.isNull(fileId)) {
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "文件ID不能为空");
        }
    }

    public void checkSaveParams() {
    }

    public void checkDeleteParams() {
        if (!CollUtil.isNotEmpty(fileIds)) {
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "文件ID列表不能为空");
        }
    }
}
