package com.abc.itbbs.blog.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 点赞记录VO对象
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
@Data
public class LikeRecordVO {

    private Long likeRecordId;

    private Integer type;

    private Long targetId;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
