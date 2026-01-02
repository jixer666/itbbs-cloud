package com.abc.itbbs.system.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long userId;

    private String username;

    private String nickname;

    private String avatar;

    private String description;

    // 用于分配角色
    private List<Long> roleIds;

    private List<Long> userIds;

    public void checkSaveUserRoleParams() {
        AssertUtils.isNotEmpty(this, "用户参数不能为空");
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(roleIds), "角色ID列表不能为空");
    }

    public void checkUpdateUserParams() {
        AssertUtils.isNotEmpty(this, "用户参数不能为空");
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
    }
}
