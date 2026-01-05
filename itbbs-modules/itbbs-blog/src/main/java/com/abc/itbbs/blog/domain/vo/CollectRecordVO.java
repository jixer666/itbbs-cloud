package com.abc.itbbs.blog.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 收藏记录VO对象
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
@Data
public class CollectRecordVO {

    private Long collectRecordId;

    private Integer type;

    private Long targetId;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
