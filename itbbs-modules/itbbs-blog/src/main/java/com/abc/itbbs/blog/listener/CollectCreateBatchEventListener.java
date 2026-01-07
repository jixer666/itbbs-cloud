package com.abc.itbbs.blog.listener;

import cn.hutool.json.JSONUtil;
import com.abc.itbbs.blog.convert.CollectRecordConvert;
import com.abc.itbbs.blog.domain.dto.CollectRecordDTO;
import com.abc.itbbs.blog.domain.entity.CollectRecord;
import com.abc.itbbs.blog.service.CollectRecordService;
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
@RabbitListener(queues = RabbitMQConstants.BLOG_COLLECT_CREATE_QUEUE)
public class CollectCreateBatchEventListener {

    @Autowired
    private CollectRecordService collectRecordService;

    /**
     * 收藏记录事件监听
     */
    @RabbitHandler
    public void handleArticleEsEvent(Set<String> collectRecordDTOSet, Message message, Channel channel) throws IOException {
        log.info("===开始消费收藏记录批量构建===");
        try {
            List<CollectRecord> collectRecordList = collectRecordDTOSet.stream().map(item -> {
                CollectRecordDTO collectRecordDTO = JSONUtil.toBean(item, CollectRecordDTO.class);
                return CollectRecordConvert.buildDefaultCollectRecordByCollectRecordDTO(collectRecordDTO);
            }).collect(Collectors.toList());

            collectRecordService.saveBatch(collectRecordList);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("消费收藏记录出错：{}", e.getMessage(), e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        log.info("===完成消费收藏记录批量构建===");
    }

}
