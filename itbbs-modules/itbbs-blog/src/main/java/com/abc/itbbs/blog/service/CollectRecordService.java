package com.abc.itbbs.blog.service;

import com.abc.itbbs.blog.domain.dto.CollectRecordDTO;
import com.abc.itbbs.blog.domain.entity.CollectRecord;
import com.abc.itbbs.blog.strategy.collectrecord.CollectRecordStrategy;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 收藏记录接口
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
public interface CollectRecordService extends IService<CollectRecord> {

    PageResult getCollectRecordPageWithUiParam(CollectRecordDTO collectRecordDTO);

    void updateCollectRecord(CollectRecordDTO collectRecordDTO);

    void saveCollectRecord(CollectRecordDTO collectRecordDTO);

    void deleteCollectRecord(CollectRecordDTO collectRecordDTO);

    void increaseRecordRecord(Long targetId, Integer biz);

    List<CollectRecord> selectCollectTargetIdsByUserId(Long userId, Boolean isLimit);

    void rebuildCollectCountCache(Long targetId, CollectRecordStrategy collectRecordStrategy);
}
