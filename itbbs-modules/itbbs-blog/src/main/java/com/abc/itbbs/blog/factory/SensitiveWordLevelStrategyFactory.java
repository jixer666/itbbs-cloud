package com.abc.itbbs.blog.factory;

import com.abc.itbbs.blog.domain.enums.SensitiveWordLevelEnum;
import com.abc.itbbs.blog.strategy.sensitiveword.SensitiveWordLevelStrategy;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SensitiveWordLevelStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, SensitiveWordLevelStrategy> LOGIN_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(SensitiveWordLevelStrategy.class).forEach((k, v)->{
            LOGIN_MAP.put(SensitiveWordLevelEnum.getTypeByClass(k), v);
        });
    }

    public static SensitiveWordLevelStrategy getSensitiveWordStrategy(Integer loginType){
        Integer type = SensitiveWordLevelEnum.getClassByType(loginType);
        AssertUtils.isNotEmpty(type, "认证方式不存在");
        return LOGIN_MAP.get(type);
    }



}
