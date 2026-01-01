package com.abc.itbbs.common.mq.config;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class RabbitMQConfig {

    private RabbitTemplate rabbitTemplate;

    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setMessageConverter(messageConverter());
        initRabbitTemplate();
        return rabbitTemplate;
    }

    public void initRabbitTemplate() {

        /**
         * 1、只要消息抵达Broker就ack=true
         * correlationData：当前消息的唯一关联数据(这个是消息的唯一id)
         * ack：消息是否成功收到
         * cause：失败的原因
         */
        //设置确认回调
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause) -> {
            System.out.println("confirm...correlationData["+correlationData+"]==>ack:["+ack+"]==>cause:["+cause+"]");
        });

        /**
         * 只要消息没有投递给指定的队列，就触发这个失败回调
         * message：投递失败的消息详细信息
         * replyCode：回复的状态码
         * replyText：回复的文本内容
         * exchange：当时这个消息发给哪个交换机
         * routingKey：当时这个消息用哪个路邮键
         */
        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routingKey) -> {
            // TODO 对于失败的消息进行重试

            System.out.println("Fail Message["+message+"]==>replyCode["+replyCode+"]" +
                    "==>replyText["+replyText+"]==>exchange["+exchange+"]==>routingKey["+routingKey+"]");
        });
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(){
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                // 判断消息不为空
                if (ObjectUtil.isNull(message)){
                    return super.fromMessage(message);
                }

                // 从消息头中取出用户信息
                String userInfoStr = message.getMessageProperties().getHeader(CommonConstants.MQ_USER_INFO);
                if (StringUtils.isEmpty(userInfoStr)){
                    return super.fromMessage(message);
                }
                // 存入上下文
                LoginUserDTO loginUserDTO = BeanUtil.toBean(userInfoStr, LoginUserDTO.class);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO, null, loginUserDTO.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                return super.fromMessage(message);
            }
        };
    }


    /**
     * 普通队列：用于消费解单消息
     * @return
     */
    @Bean
    public Queue articleCreateQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        return new Queue(RabbitMQConstants.BLOG_ARTICLE_CREATE_QUEUE, true, false, false, null);
    }

    @Bean
    public Queue articleCreateHtmlQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        return new Queue(RabbitMQConstants.BLOG_ARTICLE_HTML_QUEUE, true, false, false, null);
    }


    @Bean
    public Queue articleCreateVectorQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        return new Queue(RabbitMQConstants.BLOG_ARTICLE_VECTOR_QUEUE, true, false, false, null);
    }

    @Bean
    public Queue articleCreateEsQueue() {
        // String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        return new Queue(RabbitMQConstants.BLOG_ARTICLE_ES_QUEUE, true, false, false, null);
    }


//    /**
//     * 死信队列
//     * @return
//     */
//    @Bean
//    public Queue orderDelayQueue() {
//        /*
//            Queue(String name,  队列名字
//            boolean durable,  是否持久化
//            boolean exclusive,  是否排他
//            boolean autoDelete, 是否自动删除
//            Map<String, Object> arguments) 属性
//         */
//        HashMap<String, Object> arguments = new HashMap<>();
//        arguments.put("x-dead-letter-exchange", "order-event-exchange");
//        arguments.put("x-dead-letter-routing-key", "order.release.order");
//        arguments.put("x-message-ttl", 300000); // 消息过期时间5分钟
//        Queue queue = new Queue("order.delay.queue", true, false, false, arguments);
//
//        return queue;
//    }

    /**
     * 交换机
     * @return
     */
    @Bean
    public Exchange articleEventExchange() {
        // String name, boolean durable, boolean autoDelete
        return new TopicExchange(RabbitMQConstants.BLOG_ARTICLE_EXCHANGE, true, false);
    }

//    /**
//     * 绑定死信队列和交换机的关系
//     * @return
//     */
//    @Bean
//    public Binding orderCreateBinding() {
//        /*
//         * String destination, 目的地（队列名或者交换机名字）
//         * DestinationType destinationType, 目的地类型（Queue、Exhcange）
//         * String exchange,
//         * String routingKey,
//         * Map<String, Object> arguments
//         * */
//        return new Binding("order.delay.queue",
//                Binding.DestinationType.QUEUE,
//                "order-event-exchange",
//                "order.create.order",
//                null);
//    }


    /**
     * 绑定普通队列和交换机的关系
     * @return
     */
    @Bean
    public Binding articleCreateBinding() {
        return new Binding(RabbitMQConstants.BLOG_ARTICLE_CREATE_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitMQConstants.BLOG_ARTICLE_EXCHANGE,
                RabbitMQConstants.BLOG_ARTICLE_CREATE_KEY,
                null);
    }

    @Bean
    public Binding articleCreateHtmlBinding() {
        return new Binding(RabbitMQConstants.BLOG_ARTICLE_HTML_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitMQConstants.BLOG_ARTICLE_EXCHANGE,
                RabbitMQConstants.BLOG_ARTICLE_HTML_KEY,
                null);
    }

    @Bean
    public Binding articleCreateEsBinding() {
        return new Binding(RabbitMQConstants.BLOG_ARTICLE_ES_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitMQConstants.BLOG_ARTICLE_EXCHANGE,
                RabbitMQConstants.BLOG_ARTICLE_ES_KEY,
                null);
    }

    @Bean
    public Binding articleCreateVectorBinding() {
        return new Binding(RabbitMQConstants.BLOG_ARTICLE_VECTOR_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitMQConstants.BLOG_ARTICLE_EXCHANGE,
                RabbitMQConstants.BLOG_ARTICLE_VECTOR_KEY,
                null);
    }






}
