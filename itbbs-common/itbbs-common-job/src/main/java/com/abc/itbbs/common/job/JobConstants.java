package com.abc.itbbs.common.job;

/**
 * @author LiJunXi
 * @date 2026/1/3
 */
public class JobConstants {

    public static final String ARTICLE_COUNT_TASK_NAME = "文章计数定时任务";
    public static final String ARTICLE_COUNT_TASK_TARGET = "com.abc.itbbs.blog.task.ArticleCountTask.run()";

    public static final String LIKE_RECORD_SAVE_TASK_NAME = "点赞记录保存定时任务";
    public static final String LIKE_RECORD_SAVE_TASK_TARGET = "com.abc.itbbs.blog.task.LikeRecordSaveTask.run()";

    public static final String ARTICLE_PRELOAD_TASK_NAME = "文章信息预加载定时任务";
    public static final String ARTICLE_PRELOAD_TASK_TARGET = "com.abc.itbbs.blog.task.ArticlePreLoadTask.run()";


}
