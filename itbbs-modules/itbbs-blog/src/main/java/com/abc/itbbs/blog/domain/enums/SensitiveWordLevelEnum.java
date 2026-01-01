package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SensitiveWordLevelEnum {
    HIGH(1, "直接拦截", "highSensitiveWordLevelStrategy"),
    MID(2, "人工审核", "midSensitiveWordLevelStrategy"),
    LOW(3, "仅警告", "lowSensitiveWordLevelStrategy");

    private Integer type;
    private String desc;
    private String clasz;

    public static Integer getTypeByClass(String clasz) {
        return Arrays.stream(SensitiveWordLevelEnum.values()).filter(item -> item.getClasz().equals(clasz)).findFirst().map(SensitiveWordLevelEnum::getType).orElse(null);
    }

    public static Integer getClassByType(Integer level) {
        return Arrays.stream(SensitiveWordLevelEnum.values()).filter(item -> item.getType().equals(level)).findFirst().map(SensitiveWordLevelEnum::getType).orElse(null);
    }

    public static SensitiveWordLevelEnum getByType(Integer level) {
        return Arrays.stream(SensitiveWordLevelEnum.values()).filter(item -> item.getType().equals(level)).findFirst().orElse(null);
    }
}
