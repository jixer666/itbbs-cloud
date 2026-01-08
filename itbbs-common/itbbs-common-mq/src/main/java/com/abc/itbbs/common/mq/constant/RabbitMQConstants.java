package com.abc.itbbs.common.mq.constant;

/**
 * @author LiJunXi
 * @date 2026/1/1
 */
public class RabbitMQConstants {

    /**
     * 文章发布
     */
    public static final String BLOG_ARTICLE_EXCHANGE = "blog.article.exchange";

    public static final String BLOG_ARTICLE_CREATE_QUEUE = "blog.article.create.queue";
    public static final String BLOG_ARTICLE_CREATE_KEY = "blog.article.create";

    public static final String BLOG_ARTICLE_HTML_QUEUE = "blog.article.html.queue";
    public static final String BLOG_ARTICLE_HTML_KEY = "blog.article.html";

    public static final String BLOG_ARTICLE_ES_QUEUE = "blog.article.es.queue";
    public static final String BLOG_ARTICLE_ES_KEY = "blog.article.es";

    public static final String BLOG_ARTICLE_VECTOR_QUEUE = "blog.article.vector.queue";
    public static final String BLOG_ARTICLE_VECTOR_KEY = "blog.article.vector";

    public static final String BLOG_ARTICLE_COUNT_QUEUE = "blog.article.count.queue";
    public static final String BLOG_ARTICLE_COUNT_KEY = "blog.article.count";

    /**
     * 点赞
     */
    public static final String BLOG_LIKE_EXCHANGE = "blog.like.exchange";
    public static final String BLOG_LIKE_CREATE_QUEUE = "blog.like.create.queue";
    public static final String BLOG_LIKE_CREATE_BATCH_KEY = "blog.like.create.batch";

    /**
     * 收藏
     */
    public static final String BLOG_COLLECT_EXCHANGE = "blog.collect.exchange";
    public static final String BLOG_COLLECT_CREATE_QUEUE = "blog.collect.create.queue";
    public static final String BLOG_COLLECT_CREATE_BATCH_KEY = "blog.collect.create.batch";

    /**
     * 文章信息预加载
     */
    public static final String BLOG_ARTICLE_PRELOAD_QUEUE = "blog.article.preload.queue";
    public static final String BLOG_ARTICLE_PRELOAD_BATCH_KEY = "blog.article.preload.batch";

}
