package com.hfut.newsbackend.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/24 18:54
 */
//监听项目已启动，spring加载后执行接口一个方法
@Component
public class OSSClientUtil implements InitializingBean {

    //阿里云OSS地址，这里看根据你的oss选择
    private String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    //阿里云OSS的accessKeyId
    private String accessKeyId = "LTAI5t62rEjoYuw5nRqGNqkZ";
    //阿里云OSS的密钥
    private String accessKeySecret = "kmu7bRse7PLbwtfNQo9mRW7anaPQUt";
    //阿里云OSS上的存储块bucket名字
    private String bucketName = "newsapp-lucky";
    //阿里云图片文件存储目录
    // private String homeImageDir = "community/";

    // 定义公开静态变量
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = accessKeyId;
        ACCESS_KEY_SECRET = accessKeySecret;
        BUCKET_NAME = bucketName;
    }

}

