package com.abc.itbbs.api.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiJunXi
 * @date 2026/1/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobLogSaveDTO {

    private String jobName;

    private String invokeTarget;

    private String jobMessage;

    private String exceptionInfo;

    private Integer status;

}
