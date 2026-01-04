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

    public static Long checkArticleLikeCountKeyExist(String key1, String key2) {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(CHECK_ARTICLE_LIKE_COUNT_LUA, Long.class);
        return stringRedisTemplate.execute(redisScript, Arrays.asList(key1, key2));
    }


}
