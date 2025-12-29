package com.abc.itbbs.system.service;

import com.abc.itbbs.api.system.domain.dto.OssFileUploadDTO;
import com.abc.itbbs.api.system.domain.vo.FileVO;
import org.springframework.http.ResponseEntity;

public interface OssService {

    FileVO uploadOss(OssFileUploadDTO req);

    ResponseEntity<byte[]> downloadOss(Long fileId);

}
