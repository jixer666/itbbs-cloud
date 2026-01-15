package com.abc.itbbs.ai.constant;

public class BotConstants {

    public static final String RAG_CHAT_PROMPT = "你是一个AI助手，现在需要你严格根据所给出的内容回答我提出的问题。给出的内容按优先级从大到小排序，优先级越大越先考虑。对不确定内容说明:未找到相关信息\n" +
            "给出的内容是：%s\n" +
            "我的问题是：%s";

}
