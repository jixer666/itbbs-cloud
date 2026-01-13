package com.abc.itbbs.common.ai.strategy.chat;

import cn.hutool.extra.spring.SpringUtil;
import com.abc.itbbs.common.ai.constant.AiConstants;
import com.abc.itbbs.common.core.util.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class DeepSeekChatStrategy extends BaseChatStrategy {

    private static Environment env;
    static {
        env = SpringUtil.getBean(Environment.class);
    }

    @Override
    protected String getRequestUrl() {
        return AiConstants.DEEP_SEEK_ENDPOINT + AiConstants.DEEP_SEEK_REQUEST_PATH;
    }

    @Override
    protected String getModel() {
        String model = env.getProperty("ai.deepseek.model");
        return StringUtils.isEmpty(model) ? AiConstants.DEEP_SEEK_MODEL : model;
    }

    @Override
    protected String getApiKey() {
        return env.getProperty("ai.deepseek.api-key");
    }
}
