package com.abc.itbbs.ai.factory;

import com.abc.itbbs.ai.domain.enums.ChatModeEnum;
import com.abc.itbbs.ai.strategy.chatmode.ChatModeStrategy;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatModeStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, ChatModeStrategy> CHAT_MODE_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(ChatModeStrategy.class).forEach((k, v)->{
            CHAT_MODE_MAP.put(ChatModeEnum.getTypeByClass(k), v);
        });
    }

    public static ChatModeStrategy getChatModeStrategy(Integer loadType){
        AssertUtils.isNotEmpty(loadType, "聊天模式不能为空");
        ChatModeStrategy chatModeStrategy = CHAT_MODE_MAP.get(loadType);
        AssertUtils.isNotEmpty(chatModeStrategy, "聊天模式不存在");

        return chatModeStrategy;
    }


}
