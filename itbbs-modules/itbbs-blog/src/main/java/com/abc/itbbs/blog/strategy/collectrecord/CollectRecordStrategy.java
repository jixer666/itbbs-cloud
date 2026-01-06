package com.abc.itbbs.blog.strategy.collectrecord;

public interface CollectRecordStrategy {

    Integer getType();

    String getCountCacheKey(Long targetId);

    String getCountLockCacheKey(Long targetId);

    String getWaitDoTask();  // 待同步点收藏列表，考虑到每个业务的同步数逻辑不同，需要单独处理

    void saveCollectRecordCache(Long targetId);

}
