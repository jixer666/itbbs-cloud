package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.convert.LikeRecordConvert;
import com.abc.itbbs.blog.domain.dto.LikeRecordDTO;
import com.abc.itbbs.blog.domain.entity.LikeRecord;
import com.abc.itbbs.blog.domain.vo.LikeRecordVO;
import com.abc.itbbs.blog.mapper.LikeRecordMapper;
import com.abc.itbbs.blog.service.LikeRecordService;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 点赞记录业务处理
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
@Service
public class LikeRecordServiceImpl extends BaseServiceImpl<LikeRecordMapper, LikeRecord> implements LikeRecordService {

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Override
    public PageResult getLikeRecordPageWithUiParam(LikeRecordDTO likeRecordDTO) {
        startPage();
        List<LikeRecord> likeRecords = likeRecordMapper.selectLikeRecordList(likeRecordDTO);
        List<LikeRecordVO> likeRecordVOList = pageList2CustomList(likeRecords, (List<LikeRecord> list) -> {
            return BeanUtil.copyToList(list, LikeRecordVO.class);
        });

        return buildPageResult(likeRecordVOList);
    }

    @Override
    public void updateLikeRecord(LikeRecordDTO likeRecordDTO) {
        likeRecordDTO.checkUpdateParams();
        LikeRecord likeRecord = likeRecordMapper.selectById(likeRecordDTO.getLikeRecordId());
        AssertUtils.isNotEmpty(likeRecord, "点赞记录不存在");
        BeanUtils.copyProperties(likeRecordDTO, likeRecord);
        likeRecordMapper.updateById(likeRecord);
    }

    @Override
    public void saveLikeRecord(LikeRecordDTO likeRecordDTO) {
        likeRecordDTO.checkSaveParams();
        LikeRecord likeRecord = LikeRecordConvert.buildDefaultLikeRecordByLikeRecordDTO(likeRecordDTO);
        likeRecordMapper.insert(likeRecord);
    }

    @Override
    public void deleteLikeRecord(LikeRecordDTO likeRecordDTO) {
        likeRecordDTO.checkDeleteParams();

        likeRecordMapper.deleteBatchIds(likeRecordDTO.getLikeRecordIds());
    }
    

}