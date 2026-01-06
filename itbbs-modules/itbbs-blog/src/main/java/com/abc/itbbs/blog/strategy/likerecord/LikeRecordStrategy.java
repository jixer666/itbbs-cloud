package com.abc.itbbs.blog.strategy.likerecord;

public interface LikeRecordStrategy {

    Integer getType();

    String getCountCacheKey(Long targetId);

    String getCountLockCacheKey(Long targetId);

    String getWaitDoTask();  // 待同步点赞数列表，考虑到每个业务的同步数逻辑不同，需要单独处理

    void saveLikeRecordCache(Long targetId);

}
