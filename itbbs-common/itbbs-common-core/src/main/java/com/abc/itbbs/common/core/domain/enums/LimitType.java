package com.abc.itbbs.common.core.domain.enums;

import lombok.Getter;

@Getter
public enum LimitType {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP
}
