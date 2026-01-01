package com.abc.itbbs.common.mq.producer;

import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.module.threadlocal.ThreadLocalTempVar;
import com.abc.itbbs.common.security.util.SecurityUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/1/1
 */
@Component
public class RabbitMQProducer {

    @Value("${token.header}")
    private String tokenHeader;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String routingKey, Object msgData){
        rabbitTemplate.convertAndSend(exchange, routingKey, msgData, message -> {
            Object token = ThreadLocalTempVar.getTempTokenVar();
            if (Objects.isNull(token)){
                return message;
            }
            // 传递Token(防止MQ中再次进行远程调用导致出现请求头丢失的情况)和用户信息
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setHeader(tokenHeader, CommonConstants.TOKEN_PREFIX + token);
            messageProperties.setHeader(CommonConstants.MQ_USER_INFO, SecurityUtils.getLoginUser());
            return message;
        });
    }

}
