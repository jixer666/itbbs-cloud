package com.abc.itbbs.blog.factory;

import com.abc.itbbs.blog.domain.enums.ArticleLoadTypeEnum;
import com.abc.itbbs.blog.strategy.articleload.ArticleLoadStrategy;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ArticleLoadStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, ArticleLoadStrategy> ARTICLE_LOAD_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(ArticleLoadStrategy.class).forEach((k, v)->{
            ARTICLE_LOAD_MAP.put(ArticleLoadTypeEnum.getTypeByClass(k), v);
        });
    }

    public static ArticleLoadStrategy getArticleLoadStrategy(Integer loadType){
        AssertUtils.isNotEmpty(loadType, "文章加载类型不能为空");
        ArticleLoadStrategy articleLoadStrategy = ARTICLE_LOAD_MAP.get(loadType);
        AssertUtils.isNotEmpty(articleLoadStrategy, "文章加载类型不存在");

        return articleLoadStrategy;
    }


}
