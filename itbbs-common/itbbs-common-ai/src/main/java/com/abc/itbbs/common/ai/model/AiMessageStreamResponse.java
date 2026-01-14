package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.List;

@Data
public class AiMessageStreamResponse {

    private String id;

    private String model;

    private Integer code;

    private String sid;

    private String object;

    private Long created;

    private String message;

    private List<AiMessageResponseChoice> choices;

    @Data
    public static class AiMessageResponseChoice {

        private Integer index;

        private AiMessage delta;

    }


}
