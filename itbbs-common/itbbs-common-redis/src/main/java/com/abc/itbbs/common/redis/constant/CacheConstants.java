package com.abc.itbbs.common.redis.constant;

public class CacheConstants {

    public static final String SYSTEM_NAME = "itbbs:";

    public static final String LOGIN_TOKEN_KEY = "loginToken:%s";

    public static final String CAPTCHA_UUID = "captchaUuid:%s";
    public static final Long CAPTCHA_UUID_EXPIRE_TIME = 5L;

    public static final String EMAIL_UUID = "emailUuid:%s";
    public static final Long EMAIL_UUID_EXPIRE_TIME = 5L;

    public static final String USER_INFO = "userInfo:%d";
    public static final Long USER_INFO_EXPIRE_TIME = 3600L;

    public static final String ARTICLE_INFO = "articleInfo:%d";
    public static final Long ARTICLE_INFO_EXPIRE_TIME = 3600L;

    public static final String ARTICLE_WAIT_DO_TASK = "articleWaitDoTask";
    public static final String LIKE_RECORD_WAIT_DO_TASK = "likeRecordWaitDoTask";

    public static final String INCREASE_ARTICLE_VIEWS_COUNT = "increaseArticleViewsCount:";
    public static final String ARTICLE_VIEWS_COUNT = "articleViewsCount:%d";

    public static final String ARTICLE_LIKE_COUNT = "articleLikeCount:%d";
    public static final String USER_LIKE_SET = "userLikeSet:%s:%s";
    public static final String ARTICLE_VIEW_LOCK = "articleViewLock:%d";
    public static final String ARTICLE_LIKE_LOCK = "articleLikeLock:%d";
    public static final String USER_LIKE_SET_LOCK = "userLikeSetLock:%d";
    public static final Long USER_LIKE_SET_LENGTH = 9999L;



    public static String getFinalKey(String key, Object...values) {
        return String.format(SYSTEM_NAME + key, values);
    }





}
