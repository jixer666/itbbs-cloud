package com.abc.itbbs.common.oss.strategy;


import com.abc.itbbs.common.oss.domain.dto.OssFileDTO;
import com.abc.itbbs.common.oss.domain.entity.File;

public interface OssStrategy {

    void saveFile(OssFileDTO file);

    byte[] getFile(File fileEntity);

    void deleteFile(File fileEntity);

}
