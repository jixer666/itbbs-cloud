package com.abc.itbbs.blog.util;

import cn.hutool.extra.spring.SpringUtil;
import com.abc.itbbs.common.redis.util.RedisUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;

/**
 * @author LiJunXi
 * @date 2026/1/4
 */
public class BlogRedisUtils extends RedisUtils {

    private static StringRedisTemplate stringRedisTemplate;

    static {
        BlogRedisUtils.stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
    }

    public static final String CHECK_ARTICLE_LIKE_COUNT_LUA =
            "if redis.call('EXISTS', KEYS[1]) == 0 or redis.call('EXISTS', KEYS[2]) == 0 then\n" +
                    "    return -1 \n" +
                    "end\n";

    private static final String TOGGLE_LIKE_LUA =
            "local likeCountKey = KEYS[1] \n" +
            "local userLikeSetKey = KEYS[2] \n" +
            "local waitDoTaskKey = KEYS[3] \n" +
            "local recordTaskKey = KEYS[4] \n" +
            "local targetId = ARGV[1] \n" +
            "local userId = ARGV[2] \n" +
            "local maxLength = tonumber(ARGV[3]) \n" +
            "local expireTime = ARGV[4] \n" +
            "local biz = ARGV[5] \n" +
            "local timestamp = ARGV[6] \n" +
            "-- 检查是否已点赞 \n" +
            "local score = redis.call('ZSCORE', userLikeSetKey, targetId) \n" +
            "if not score then \n" +
            "    -- ========== 点赞逻辑 ========== \n" +
            "    -- 增加点赞计数  \n" +
            "    redis.call('INCR', likeCountKey) \n" +
            "    redis.call('EXPIRE', likeCountKey, expireTime * 3600) \n" +
            "    -- 添加到用户点赞集合 \n" +
            "    redis.call('ZADD', userLikeSetKey, timestamp, targetId) \n" +
            "    -- 维护用户点赞集合大小 \n" +
            "    local setSize = redis.call('ZCARD', userLikeSetKey) \n" +
            "    if setSize > maxLength then \n" +
             "        -- 超过了最大点赞长度，抛弃第一个 \n" +
             "        redis.call('ZREMRANGEBYRANK', userLikeSetKey, 0, setSize - maxLength - 1) \n" +
            "    end \n" +
            "    -- 点赞数加入到待同步集合 \n" +
            "    redis.call('ZADD', waitDoTaskKey, timestamp, targetId) \n" +
            "    -- 点赞记录加入到待同步集合 \n" +
            "    local record = '{\"targetId\":' .. targetId .. ',\"biz\":' .. biz .. ',\"userId\":' .. userId .. ', \"type\": 1}' \n" +
            "    redis.call('ZADD', recordTaskKey, timestamp, record) \n" +
            "    return 1 -- 点赞成功 \n" +
            "else \n" +
            "    -- ========== 取消点赞逻辑 ========== \n" +
            "    -- 减少点赞计数（确保不为负数） \n" +
            "    local current = redis.call('GET', likeCountKey) \n" +
            "    if current and tonumber(current) > 0 then \n" +
            "        redis.call('DECR', likeCountKey) \n" +
            "    else \n" +
            "        redis.call('DEL', likeCountKey) \n" +
            "    end \n" +
            "    redis.call('EXPIRE', likeCountKey, expireTime * 3600) \n" +
            "    -- 从用户点赞集合移除 \n" +
            "    redis.call('ZREM', userLikeSetKey, targetId) \n" +
            "    -- 添加到取消点赞待处理任务 \n" +
            "    redis.call('ZADD', waitDoTaskKey, timestamp, targetId) \n" +
            "    -- 构建取消点赞记录 \n" +
            "    local record = '{\"targetId\":' .. targetId .. ',\"biz\":' .. biz .. ',\"userId\":' .. userId .. ', \"type\": 2}' \n" +
            "    redis.call('ZADD', recordTaskKey, timestamp, record) \n" +
            "    return 0 -- 取消点赞成功 \n" +
            "end";

    public static Long checkArticleLikeCountKeyExist(String key1, String key2) {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(CHECK_ARTICLE_LIKE_COUNT_LUA, Long.class);
        return stringRedisTemplate.execute(redisScript, Arrays.asList(key1, key2));
    }

    public static Long toggleLikeOrCollectByLua(String likeCountKey, String userLikeSetKey, String waitDoTaskKey, String recordTaskKey,
                                                Long targetId, Long userId, Long maxLength, Long expireTime, Integer biz, Long timestamp) {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(TOGGLE_LIKE_LUA, Long.class);
        return stringRedisTemplate.execute(redisScript,
                Arrays.asList(likeCountKey, userLikeSetKey, waitDoTaskKey, recordTaskKey),
                targetId.toString(), userId.toString(), maxLength.toString(), expireTime.toString(), biz.toString(), timestamp.toString());
    }


}
