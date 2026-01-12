package com.abc.itbbs.common.ai.service;

import cn.hutool.extra.spring.SpringUtil;
import com.abc.itbbs.common.ai.constant.AiConstants;
import org.springframework.core.env.Environment;

public class DeepSeekChatService extends BaseChatService {

    @Override
    protected String getRequestUrl() {
        return AiConstants.DEEP_SEEK_ENDPOINT + AiConstants.DEEP_SEEK_REQUEST_PATH;
    }

    @Override
    protected String getModel() {
        return AiConstants.DEEP_SEEK_MODEL;
    }

    @Override
    protected String getApiKey() {
        Environment env = SpringUtil.getBean(Environment.class);
        return env.getProperty("");
    }
}
