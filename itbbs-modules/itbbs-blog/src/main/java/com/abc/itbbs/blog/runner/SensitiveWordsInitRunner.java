package com.abc.itbbs.blog.runner;

import com.abc.itbbs.blog.domain.dto.algorithm.SensitiveWordTrie;
import com.abc.itbbs.blog.domain.entity.SensitiveWord;
import com.abc.itbbs.blog.service.SensitiveWordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class SensitiveWordsInitRunner implements ApplicationRunner {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("开始加载敏感词数据");
        List<SensitiveWord> sensitiveWordList = sensitiveWordService.getSensitiveWordList();
        new SensitiveWordTrie().buildTrie(sensitiveWordList);
        log.info("结束加载敏感词");
    }

}
