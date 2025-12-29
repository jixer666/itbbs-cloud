package com.abc.itbbs.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.common.oss.domain.dto.FileDTO;
import com.abc.itbbs.common.oss.domain.dto.OssFileDTO;
import com.abc.itbbs.api.system.domain.dto.OssFileUploadDTO;
import com.abc.itbbs.common.oss.domain.entity.File;
import com.abc.itbbs.common.security.util.SecurityUtils;
import com.abc.itbbs.api.system.domain.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件转换器
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Slf4j
public class FileConvert {

    public static File buildDefaultFileByFileDTO(FileDTO fileDTO) {
        File file = BeanUtil.copyProperties(fileDTO, File.class);
        file.setFileId(IdUtils.getId());
        file.setCommonParams();

        return file;
    }

    public static OssFileDTO buildByFile(File fileEntity, MultipartFile file) {
        OssFileDTO ossFileDto = BeanUtil.copyProperties(fileEntity, OssFileDTO.class);
        try {
            ossFileDto.setFileData(file.getBytes());
            ossFileDto.setMultipartFile(file);
        } catch (Exception e){
            log.error("创建OSSFileDto对象出错，原因：{}", e.getMessage());
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "创建对象出错");
        }
        return ossFileDto;
    }

    public static File buildOtherFile(File fileEntity) {
        File saveFile = BeanUtil.copyProperties(fileEntity, File.class);
        saveFile.setFileId(IdUtils.getId());
        saveFile.setUserId(SecurityUtils.getUserId());
        saveFile.setCommonParams();

        return saveFile;
    }

    public static File buildNewFileByReq(OssFileUploadDTO req, Integer ossType) {
        File fileEntity = BeanUtil.copyProperties(req, File.class);
        fileEntity.setFileId(IdUtils.getId());
        fileEntity.setOssType(ossType);
        fileEntity.setTotalSize(req.getFile().getSize());
        fileEntity.setUserId(SecurityUtils.getUserId());
        fileEntity.setFilename(req.getFile().getOriginalFilename());
        fileEntity.setCommonParams();

        return fileEntity;
    }

    public static FileVO buildFileVO(File saveFile, String downloadUrl) {
        FileVO fileVo = BeanUtil.copyProperties(saveFile, FileVO.class);
        fileVo.setDownloadUrl(downloadUrl);

        return fileVo;
    }

}
