package com.abc.itbbs.common.email.factory;

import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.email.constant.EmailConstants;
import com.abc.itbbs.common.email.domain.enums.EmailTypeEnum;
import com.abc.itbbs.common.email.strategy.EmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmailStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, EmailStrategy> EMAIL_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(EmailStrategy.class).forEach((k, v)->{
            EMAIL_MAP.put(EmailTypeEnum.getTypeByClass(k), v);
        });
    }

    public static EmailStrategy getEmailStrategy(Integer emailType){
        Integer type = EmailTypeEnum.getClassByType(emailType);
        if (Objects.isNull(type)){
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "邮箱类型不存在");
        }
        return EMAIL_MAP.get(type);
    }



}
