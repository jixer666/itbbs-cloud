package com.abc.itbbs.blog.task;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.job.JobConstants;
import com.abc.itbbs.common.job.TaskHelper;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.abc.itbbs.common.mq.producer.RabbitMQProducer;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 点赞记录保存定时任务
 */
@Slf4j
@Component
public class LikeRecordSaveTask {

    @Autowired
    private TaskHelper taskHelper;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        taskHelper.run(JobConstants.LIKE_RECORD_SAVE_TASK_NAME, JobConstants.LIKE_RECORD_SAVE_TASK_TARGET, task -> {
            String likeRecordWaitDoSetCacheKey = CacheConstants.getFinalKey(CacheConstants.LIKE_RECORD_WAIT_DO_TASK);
            Set<String> likeRecordDtoStrSet = RedisUtils.zRange(likeRecordWaitDoSetCacheKey, 0, 299);
            if (CollUtil.isEmpty(likeRecordDtoStrSet)) {
                return null;
            }

            rabbitMQProducer.sendMessage(RabbitMQConstants.BLOG_LIKE_EXCHANGE, RabbitMQConstants.BLOG_LIKE_CREATE_BATCH_KEY, likeRecordDtoStrSet);

            RedisUtils.zRemoveRange(likeRecordWaitDoSetCacheKey, 0, likeRecordDtoStrSet.size() - 1);

            return task;
        }, task -> {
            return task;
        });
    }
}
