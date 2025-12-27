package com.abc.itbbs.system.service;

import com.abc.itbbs.common.oss.domain.dto.OssFileUploadDTO;
import com.abc.itbbs.system.domain.vo.FileVO;
import org.springframework.http.ResponseEntity;

public interface OssService {

    FileVO uploadOss(OssFileUploadDTO req);

    ResponseEntity<byte[]> downloadOss(Long fileId);

}
