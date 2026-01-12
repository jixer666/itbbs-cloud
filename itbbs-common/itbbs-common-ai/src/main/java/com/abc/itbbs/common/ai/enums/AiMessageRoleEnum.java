package com.abc.itbbs.common.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@Getter
@AllArgsConstructor
public enum AiMessageRoleEnum {

    USER("user"),
    ASSISTANT("assistant");

    private String role;

}
