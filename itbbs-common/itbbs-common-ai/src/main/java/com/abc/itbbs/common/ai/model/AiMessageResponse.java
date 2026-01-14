package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AiMessageResponse {

    private String model;

    private String created_at;

    private AiMessage message;


}
