package com.abc.itbbs.blog.mapper;

import com.abc.itbbs.blog.domain.dto.LikeRecordDTO;
import com.abc.itbbs.blog.domain.entity.LikeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 点赞记录Mapper接口
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
@Mapper
public interface LikeRecordMapper extends BaseMapper<LikeRecord> {
    List<LikeRecord> selectLikeRecordList(LikeRecordDTO likeRecordDTO);

    List<LikeRecord> selectLikeTargetIdsByUserId(@Param("userId") Long userId, @Param("isLimit") Boolean isLimit);
}
