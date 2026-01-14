package com.abc.itbbs.ai.factory;

import com.abc.itbbs.ai.strategy.documentbiz.DocumentBizStrategy;
import com.abc.itbbs.api.ai.domain.enums.DocumentBizEnum;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DocumentBizStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, DocumentBizStrategy> DOCUMENT_BIZ_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(DocumentBizStrategy.class).forEach((k, v)->{
            DOCUMENT_BIZ_MAP.put(DocumentBizEnum.getBizByClass(k), v);
        });
    }

    public static DocumentBizStrategy getDocumentBizStrategy(Integer biz){
        AssertUtils.isNotEmpty(biz, "文档业务不能为空");
        DocumentBizStrategy documentBizStrategy = DOCUMENT_BIZ_MAP.get(biz);
        AssertUtils.isNotEmpty(documentBizStrategy, "文档业务不存在");

        return documentBizStrategy;
    }


}
