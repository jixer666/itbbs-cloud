package com.abc.itbbs.ai.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotChatStepEnum {

    FIRST(1, "问题分析"),
    SECOND(2, "回答问题"),
    THIRD(3, "完成回答"),
    FOURTH(4, "参考内容"),
    START(0, "开始"),
    END(-1, "结束");

    private Integer step;
    private String desc;

}
