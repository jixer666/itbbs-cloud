package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/1/14
 */
@Data
public class EmbeddingResponse {

    private List<Float> embedding;

}
