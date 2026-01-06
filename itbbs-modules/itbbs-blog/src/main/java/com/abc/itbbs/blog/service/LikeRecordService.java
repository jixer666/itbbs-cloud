package com.abc.itbbs.blog.service;

import com.abc.itbbs.blog.strategy.likerecord.LikeRecordStrategy;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.LikeRecordDTO;
import com.abc.itbbs.blog.domain.entity.LikeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 点赞记录接口
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
public interface LikeRecordService extends IService<LikeRecord> {

    PageResult getLikeRecordPageWithUiParam(LikeRecordDTO likeRecordDTO);

    void updateLikeRecord(LikeRecordDTO likeRecordDTO);

    void saveLikeRecord(LikeRecordDTO likeRecordDTO);

    void deleteLikeRecord(LikeRecordDTO likeRecordDTO);

    List<LikeRecord> selectLikeTargetIdsByUserId(Long userId, Boolean isLimit);

    void increaseLikeRecord(Long targetId, Integer biz);

    void rebuildLikeCountCache(Long targetId, LikeRecordStrategy likeRecordStrategy);

}
