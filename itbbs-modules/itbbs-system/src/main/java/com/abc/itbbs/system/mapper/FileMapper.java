package com.abc.itbbs.system.mapper;

import com.abc.itbbs.common.oss.domain.dto.FileDTO;
import com.abc.itbbs.common.oss.domain.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文件Mapper接口
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {
    List<File> selectFileList(FileDTO fileDTO);
}
