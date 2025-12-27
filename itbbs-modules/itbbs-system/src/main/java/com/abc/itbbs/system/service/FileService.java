package com.abc.itbbs.system.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.oss.domain.dto.FileDTO;
import com.abc.itbbs.common.oss.domain.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文件接口
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
public interface FileService extends IService<File> {

    PageResult getFilePageWithUiParam(FileDTO fileDTO);

    void updateFile(FileDTO fileDTO);

    void saveFile(FileDTO fileDTO);

    void deleteFile(FileDTO fileDTO);

    File checkAndGet(Long fileId);

}
