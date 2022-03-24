package com.hfut.newsbackend.controller;

import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.impl.OssServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/24 19:14
 */
@RestController
@CrossOrigin
public class OssController {

    @Autowired
    private OssServiceImpl ossService ;

    @PostMapping("/upload")
    public ResponseResult upload(@RequestBody MultipartFile file) {
        return new ResponseResult(200 , "上传成功" , ossService.publishImgToOSS(file)) ;
    }

}
