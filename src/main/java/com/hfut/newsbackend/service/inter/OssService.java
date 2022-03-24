package com.hfut.newsbackend.service.inter;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String publishImgToOSS(MultipartFile file);
}

