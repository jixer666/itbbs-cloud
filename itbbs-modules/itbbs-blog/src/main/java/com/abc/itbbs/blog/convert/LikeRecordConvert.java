package com.abc.itbbs.blog.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.domain.dto.LikeRecordDTO;
import com.abc.itbbs.blog.domain.entity.LikeRecord;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;

/**
 * 点赞记录转换器
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
public class LikeRecordConvert {
    public static LikeRecord buildDefaultLikeRecordByLikeRecordDTO(LikeRecordDTO likeRecordDTO) {
        LikeRecord likeRecord = BeanUtil.copyProperties(likeRecordDTO, LikeRecord.class);
        likeRecord.setLikeRecordId(IdUtils.getId());
        likeRecord.setUserId(SecurityUtils.getUserId());
        likeRecord.setInsertParams();

        return likeRecord;
    }

    public static LikeRecordDTO buildDefaultLikeRecordDTO(Long targetId, Integer type) {
        LikeRecordDTO likeRecordDTO = new LikeRecordDTO();
        likeRecordDTO.setTargetId(targetId);
        likeRecordDTO.setType(type);
        likeRecordDTO.setUserId(SecurityUtils.getUserId());

        return likeRecordDTO;
    }
}
