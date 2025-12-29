package com.abc.itbbs.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.oss.domain.dto.FileDTO;
import com.abc.itbbs.common.oss.domain.entity.File;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.system.convert.FileConvert;
import com.abc.itbbs.api.system.domain.vo.FileVO;
import com.abc.itbbs.system.mapper.FileMapper;
import com.abc.itbbs.system.service.FileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件业务处理
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Service
public class FileServiceImpl extends BaseServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    public PageResult getFilePageWithUiParam(FileDTO fileDTO) {
        startPage();
        List<File> files = fileMapper.selectFileList(fileDTO);
        List<FileVO> fileVOList = pageList2CustomList(files, (List<File> list) -> {
            return BeanUtil.copyToList(list, FileVO.class);
        });

        return buildPageResult(fileVOList);
    }

    @Override
    public void updateFile(FileDTO fileDTO) {
        fileDTO.checkUpdateParams();
        File file = fileMapper.selectById(fileDTO.getFileId());
        AssertUtils.isNotEmpty(file, "文件不存在");
        BeanUtils.copyProperties(fileDTO, file);
        fileMapper.updateById(file);
    }

    @Override
    public void saveFile(FileDTO fileDTO) {
        fileDTO.checkSaveParams();
        File file = FileConvert.buildDefaultFileByFileDTO(fileDTO);
        fileMapper.insert(file);
    }

    @Override
    public void deleteFile(FileDTO fileDTO) {
        fileDTO.checkDeleteParams();

        fileMapper.deleteBatchIds(fileDTO.getFileIds());
    }

    @Override
    public File checkAndGet(Long fileId) {
        AssertUtils.isNotEmpty(fileId, "文件ID不能为空");
        File fileEntity = fileMapper.selectById(fileId);
        AssertUtils.isNotEmpty(fileEntity, "文件不存在");
        return fileEntity;
    }



}