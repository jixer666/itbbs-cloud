package com.abc.itbbs.system.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRoleVO {

    private List<RoleVO> roles;

    private List<Long> roleIds;

}
