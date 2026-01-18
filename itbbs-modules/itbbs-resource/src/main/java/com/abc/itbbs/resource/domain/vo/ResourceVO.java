package com.abc.itbbs.resource.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 资源VO对象
 *
 * @author LiJunXi
 * @date 2026-01-18
 */
@Data
public class ResourceVO {

    private Long resourceId;

    private String resourceName;

    private Integer type;

    private Integer category;

    private String description;

    private Integer fileCount;

    private Long userId;

    private Integer point;

    private Integer downloadCount;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
