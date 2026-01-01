package com.abc.itbbs.blog.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 敏感词VO对象
 *
 * @author LiJunXi
 * @date 2025-12-31
 */
@Data
public class SensitiveWordVO {

    private Long wordId;

    private String word;

    private Integer level;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
