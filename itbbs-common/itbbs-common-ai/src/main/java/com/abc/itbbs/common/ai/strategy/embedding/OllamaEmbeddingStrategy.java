package com.abc.itbbs.common.ai.strategy.embedding;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.common.ai.constant.AiConstants;
import com.abc.itbbs.common.ai.model.EmbeddingRequest;
import com.abc.itbbs.common.ai.model.EmbeddingResponse;
import com.abc.itbbs.common.ai.util.HttpUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OllamaEmbeddingStrategy implements EmbeddingStrategy {

    private static Environment env;
    static {
        env = SpringUtil.getBean(Environment.class);
    }

    @Override
    public String getRequestUrl() {
        String endpoint = env.getProperty("embedding.ollama.uri");
        return endpoint + AiConstants.EMBEDDING_OLLAMA_REQUEST_PATH;
    }

    @Override
    public String getModel() {
        String model = env.getProperty("embedding.ollama.model");
        return StringUtils.isEmpty(model) ? AiConstants.EMBEDDING_OLLAMA_DEFAULT_MODEL : model;
    }

    @Override
    public List<Float> getVectorList(String text) {
        EmbeddingRequest embeddingRequest = buildEmbeddingRequest(text);
        String responseStr = HttpUtils.post(getRequestUrl(), JSONUtil.toJsonStr(embeddingRequest));

        List<Float> result = new ArrayList<>();
        try {
            EmbeddingResponse response = JSONUtil.toBean(responseStr, EmbeddingResponse.class);
            result = response.getEmbedding();
        } catch (Exception e) {
            log.error("文本嵌入模型出错：{}", e.getMessage(), e);
        }

        return result;
    }

}
