package com.abc.itbbs.common.core.domain.entity;

import com.abc.itbbs.common.core.domain.enums.StatusEnum;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity自定义基类
 */
@Data
public class BaseEntity implements Serializable {

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer ver;

    private Integer status;

    public void setCommonParams() {
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setStatus(StatusEnum.NORMAL.getStatus());
        this.setVer(CommonConstants.DEFAULT_VER);
    }

}
