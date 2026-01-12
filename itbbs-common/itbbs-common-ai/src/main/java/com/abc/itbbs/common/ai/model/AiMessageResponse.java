package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.Date;

@Data
public class AiMessageResponse {

    private String model;

    private Date created_at;

    private AiMessage message;

}
