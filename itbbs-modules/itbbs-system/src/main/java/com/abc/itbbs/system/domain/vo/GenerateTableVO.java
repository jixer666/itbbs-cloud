package com.abc.itbbs.system.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class GenerateTableVO {

    private Long genTableId;

    private String tableName;

    private String tableComment;

    private Date createTime;

    private Date updateTime;

    private Integer status;


}
