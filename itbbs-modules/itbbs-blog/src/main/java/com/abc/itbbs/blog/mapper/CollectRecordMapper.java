package com.abc.itbbs.blog.mapper;

import com.abc.itbbs.blog.domain.dto.CollectRecordDTO;
import com.abc.itbbs.blog.domain.entity.CollectRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 收藏记录Mapper接口
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
@Mapper
public interface CollectRecordMapper extends BaseMapper<CollectRecord> {
    List<CollectRecord> selectCollectRecordList(CollectRecordDTO collectRecordDTO);

    List<CollectRecord> selectCollectTargetIdsByUserId(Long userId, Boolean isLimit);
}
