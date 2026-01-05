package com.abc.itbbs.blog.service;

import com.abc.itbbs.blog.domain.dto.CollectRecordDTO;
import com.abc.itbbs.blog.domain.entity.CollectRecord;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
