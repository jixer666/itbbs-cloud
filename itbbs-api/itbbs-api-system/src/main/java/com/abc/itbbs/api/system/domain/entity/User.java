package com.abc.itbbs.api.system.domain.entity;

import com.abc.itbbs.common.core.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("tb_user")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @TableId
    private Long userId;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private String email;

    private String description;

}
