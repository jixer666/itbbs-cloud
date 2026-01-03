package com.abc.itbbs.system.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 定时任务日志VO对象
 *
 * @author LiJunXi
 * @date 2026-01-03
 */
@Data
public class JobLogVO {

    private Long jobLogId;

    private String jobName;

    private String invokeTarget;

    private String jobMessage;

    private String exceptionInfo;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
