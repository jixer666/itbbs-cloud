package com.abc.itbbs.common.ai.factory;

import com.abc.itbbs.common.ai.enums.ChatModelTypeEnum;
import com.abc.itbbs.common.ai.strategy.chat.ChatStrategy;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, ChatStrategy> CHAT_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(ChatStrategy.class).forEach((k, v)->{
            CHAT_MAP.put(ChatModelTypeEnum.getTypeByClass(k), v);
        });
    }

    public static ChatStrategy getChatStrategy(Integer type){
        AssertUtils.isNotEmpty(type, "AI聊天不能为空");
        ChatStrategy chatStrategy = CHAT_MAP.get(type);
        AssertUtils.isNotEmpty(chatStrategy, "AI聊天不存在");

        return chatStrategy;
    }



}
