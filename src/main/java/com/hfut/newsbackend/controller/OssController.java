package com.hfut.newsbackend.controller;

import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.impl.OssServiceImpl;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("用户上传图片的接口，上传后的图片存入阿里云的oss存储")
    public String upload(@RequestBody MultipartFile file) {
        return ossService.publishImgToOSS(file) ;
    }

}
