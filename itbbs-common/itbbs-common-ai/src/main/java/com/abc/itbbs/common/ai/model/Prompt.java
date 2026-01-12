package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.List;

@Data
public class Prompt {

    private List<AiMessage> messages;

}