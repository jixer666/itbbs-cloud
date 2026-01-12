package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatRequestBody {

    private String model;

    private List<AiMessage> messages;

    private Boolean stream;

}
