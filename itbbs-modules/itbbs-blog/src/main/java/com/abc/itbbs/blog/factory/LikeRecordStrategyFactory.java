package com.abc.itbbs.blog.factory;

import com.abc.itbbs.blog.domain.enums.LikeBizEnum;
import com.abc.itbbs.blog.strategy.likerecord.LikeRecordStrategy;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LikeRecordStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, LikeRecordStrategy> LOGIN_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(LikeRecordStrategy.class).forEach((k, v)->{
            LOGIN_MAP.put(LikeBizEnum.getBizByClass(k), v);
        });
    }

    public static LikeRecordStrategy getLikeRecordStrategy(Integer type){
        AssertUtils.isNotEmpty(type, "点赞业务不能为空");
        LikeRecordStrategy likeRecordStrategy = LOGIN_MAP.get(type);
        AssertUtils.isNotEmpty(likeRecordStrategy, "点赞业务不存在");

        return likeRecordStrategy;
    }



}
