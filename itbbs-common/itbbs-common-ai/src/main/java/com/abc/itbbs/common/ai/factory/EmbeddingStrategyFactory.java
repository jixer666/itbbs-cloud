package com.abc.itbbs.common.ai.factory;

import com.abc.itbbs.common.ai.enums.EmbeddingTypeEnum;
import com.abc.itbbs.common.ai.strategy.embedding.EmbeddingStrategy;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmbeddingStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, EmbeddingStrategy> EMBEDDING_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(EmbeddingStrategy.class).forEach((k, v)->{
            EMBEDDING_MAP.put(EmbeddingTypeEnum.getTypeByClass(k), v);
        });
    }

    public static EmbeddingStrategy getChatStrategy(Integer type){
        AssertUtils.isNotEmpty(type, "文本嵌入模型类型不能为空");
        EmbeddingStrategy embeddingStrategy = EMBEDDING_MAP.get(type);
        AssertUtils.isNotEmpty(embeddingStrategy, "文本嵌入模型类型不存在");

        return embeddingStrategy;
    }



}
