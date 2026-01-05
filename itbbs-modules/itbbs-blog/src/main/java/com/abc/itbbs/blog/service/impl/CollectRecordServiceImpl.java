package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.blog.convert.CollectRecordConvert;
import com.abc.itbbs.blog.domain.dto.CollectRecordDTO;
import com.abc.itbbs.blog.domain.entity.CollectRecord;
import com.abc.itbbs.blog.domain.vo.CollectRecordVO;
import com.abc.itbbs.blog.mapper.CollectRecordMapper;
import com.abc.itbbs.blog.service.CollectRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 收藏记录业务处理
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
@Service
public class CollectRecordServiceImpl extends BaseServiceImpl<CollectRecordMapper, CollectRecord> implements CollectRecordService {

    @Autowired
    private CollectRecordMapper collectRecordMapper;

    @Override
    public PageResult getCollectRecordPageWithUiParam(CollectRecordDTO collectRecordDTO) {
        startPage();
        List<CollectRecord> collectRecords = collectRecordMapper.selectCollectRecordList(collectRecordDTO);
        List<CollectRecordVO> collectRecordVOList = pageList2CustomList(collectRecords, (List<CollectRecord> list) -> {
            return BeanUtil.copyToList(list, CollectRecordVO.class);
        });

        return buildPageResult(collectRecordVOList);
    }

    @Override
    public void updateCollectRecord(CollectRecordDTO collectRecordDTO) {
        collectRecordDTO.checkUpdateParams();
        CollectRecord collectRecord = collectRecordMapper.selectById(collectRecordDTO.getCollectRecordId());
        AssertUtils.isNotEmpty(collectRecord, "收藏记录不存在");
        BeanUtils.copyProperties(collectRecordDTO, collectRecord);
        collectRecordMapper.updateById(collectRecord);
    }

    @Override
    public void saveCollectRecord(CollectRecordDTO collectRecordDTO) {
        collectRecordDTO.checkSaveParams();
        CollectRecord collectRecord = CollectRecordConvert.buildDefaultCollectRecordByCollectRecordDTO(collectRecordDTO);
        collectRecordMapper.insert(collectRecord);
    }

    @Override
    public void deleteCollectRecord(CollectRecordDTO collectRecordDTO) {
        collectRecordDTO.checkDeleteParams();

        collectRecordMapper.deleteBatchIds(collectRecordDTO.getCollectRecordIds());
    }
    

}