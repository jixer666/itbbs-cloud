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
    public static final String ARTICLE_HASH_INFO = "articleHashInfo:%d";
    public static final Long ARTICLE_INFO_EXPIRE_TIME = 3600L;

    public static final String ARTICLE_COUNT_WAIT_DO_TASK = "articleWaitDoTask";
    public static final String LIKE_RECORD_WAIT_DO_TASK = "likeRecordWaitDoTask";
    public static final String COLLECT_RECORD_WAIT_DO_TASK = "collectRecordWaitDoTask";

    public static final String ARTICLE_VIEWS_COUNT = "articleViewsCount:%d";
    public static final String ARTICLE_VIEW_COUNT_LOCK = "articleViewCountLock:%d";

    public static final String ARTICLE_LIKE_COUNT = "articleLikeCount:%d";
    public static final Long ARTICLE_LIKE_COUNT_EXPIRE_TIME = 24L;
    public static final String ARTICLE_LIKE_COUNT_LOCK = "articleLikeCountLock:%d";

    public static final String USER_LIKE_SET = "userLikeSet:%d:%d";
    public static final String USER_LIKE_SET_LOCK = "userLikeSetLock:%d";
    public static final Long USER_LIKE_SET_MAX_LENGTH = 9999L;

    public static final String ARTICLE_COLLECT_COUNT = "articleCollectCount:%d";
    public static final Long ARTICLE_COLLECT_COUNT_EXPIRE_TIME = 24L;
    public static final String ARTICLE_COLLECT_COUNT_LOCK = "articleCollectCountLock:%d";

    public static final String USER_COLLECT_SET = "userCollectSet:%d:%d";
    public static final String USER_COLLECT_SET_LOCK = "userCollectSetLock:%d";
    public static final Long USER_COLLECT_SET_MAX_LENGTH = 9999L;

    public static final String ARTICLE_PRELOAD_LOCK_KEY = "articlePreloadLock";
    public static final Long ARTICLE_PRELOAD_LOCK_EXPIRE_TIME = 10L;

    public static final String ARTICLE_NEW_LIST = "articleNewList";
    public static final Long ARTICLE_NEW_LIST_EXPIRE_TIME = 24L;

    public static final String ARTICLE_HOT_LIST = "articleHotList";
    public static final Long ARTICLE_HOT_LIST_EXPIRE_TIME = 3L;


    public static String getFinalKey(String key, Object...values) {
        return String.format(SYSTEM_NAME + key, values);
    }


}
