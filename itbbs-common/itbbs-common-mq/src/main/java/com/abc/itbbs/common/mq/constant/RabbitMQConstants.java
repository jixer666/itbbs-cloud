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



}
