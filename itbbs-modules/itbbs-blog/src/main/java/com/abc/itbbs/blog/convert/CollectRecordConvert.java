package com.abc.itbbs.blog.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.domain.dto.CollectRecordDTO;
import com.abc.itbbs.blog.domain.entity.CollectRecord;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;

import java.util.Objects;

/**
 * 收藏记录转换器
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
public class CollectRecordConvert {
    public static CollectRecord buildDefaultCollectRecordByCollectRecordDTO(CollectRecordDTO collectRecordDTO) {
        CollectRecord collectRecord = BeanUtil.copyProperties(collectRecordDTO, CollectRecord.class);
        collectRecord.setCollectRecordId(IdUtils.getId());
        if (Objects.isNull(collectRecord.getUserId())) {
            collectRecord.setUserId(SecurityUtils.getUserId());
        }
        collectRecord.setInsertParams();

        return collectRecord;
    }
}
