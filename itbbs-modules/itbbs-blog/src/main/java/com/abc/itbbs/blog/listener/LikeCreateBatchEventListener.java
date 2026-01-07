package com.abc.itbbs.blog.listener;

import cn.hutool.json.JSONUtil;
import com.abc.itbbs.blog.convert.LikeRecordConvert;
import com.abc.itbbs.blog.domain.dto.LikeRecordDTO;
import com.abc.itbbs.blog.domain.entity.LikeRecord;
import com.abc.itbbs.blog.service.LikeRecordService;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LiJunXi
 * @date 2026/1/1
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMQConstants.BLOG_LIKE_CREATE_QUEUE)
public class LikeCreateBatchEventListener {

    @Autowired
    private LikeRecordService likeRecordService;

    /**
     * 点赞记录事件监听
     */
    @RabbitHandler
    public void handleArticleEsEvent(Set<String> likeRecordDTOSet, Message message, Channel channel) throws IOException {
        log.info("===开始消费点赞记录批量构建===");
        try {
            List<LikeRecord> likeRecordList = likeRecordDTOSet.stream().map(item -> {
                LikeRecordDTO likeRecordDTO = JSONUtil.toBean(item, LikeRecordDTO.class);
                return LikeRecordConvert.buildDefaultLikeRecordByLikeRecordDTO(likeRecordDTO);
            }).collect(Collectors.toList());

            likeRecordService.saveBatch(likeRecordList);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("消费点赞记录出错：{}", e.getMessage(), e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        log.info("===完成消费点赞记录批量构建===");
    }

}
