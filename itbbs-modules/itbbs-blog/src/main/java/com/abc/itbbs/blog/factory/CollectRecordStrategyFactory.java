package com.abc.itbbs.blog.factory;

import com.abc.itbbs.blog.domain.enums.CollectBizEnum;
import com.abc.itbbs.blog.strategy.collectrecord.CollectRecordStrategy;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CollectRecordStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, CollectRecordStrategy> COLLECT_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(CollectRecordStrategy.class).forEach((k, v)->{
            COLLECT_MAP.put(CollectBizEnum.getBizByClass(k), v);
        });
    }

    public static CollectRecordStrategy getCollectRecordStrategy(Integer biz){
        AssertUtils.isNotEmpty(biz, "收藏业务不能为空");
        CollectRecordStrategy collectRecordStrategy = COLLECT_MAP.get(biz);
        AssertUtils.isNotEmpty(collectRecordStrategy, "收藏业务不存在");

        return collectRecordStrategy;
    }



}
