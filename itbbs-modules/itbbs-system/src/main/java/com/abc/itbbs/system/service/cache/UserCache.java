package com.abc.itbbs.system.service.cache;

import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.common.redis.cache.AbstractRedisStringCache;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LiJunXi
 * @date 2025/12/30
 */
@Component
public class UserCache extends AbstractRedisStringCache<Long, User> {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected String getKey(Long id) {
        return CacheConstants.getFinalKey(CacheConstants.USER_INFO, id);
    }

    @Override
    protected Map<Long, User> load(List<Long> ids) {
        List<User> users = userMapper.selectByIds(ids);
        return users.stream().collect(Collectors.toMap(User::getUserId, Function.identity()));
    }

    @Override
    protected Long getExpireSeconds() {
        return CacheConstants.USER_INFO_EXPIRE_TIME;
    }
}
