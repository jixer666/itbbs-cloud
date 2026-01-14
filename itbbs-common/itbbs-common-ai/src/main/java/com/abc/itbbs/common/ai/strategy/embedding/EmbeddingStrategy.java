package com.abc.itbbs.common.ai.strategy.embedding;

import com.abc.itbbs.common.ai.model.EmbeddingRequest;

import java.util.List;

public interface EmbeddingStrategy {

    String getRequestUrl();

    String getModel();

    List<Float> getVectorList(String text);

    default EmbeddingRequest buildEmbeddingRequest(String text) {
        return new EmbeddingRequest(getModel(), text);
    }

}
