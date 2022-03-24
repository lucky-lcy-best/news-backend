package com.hfut.newsbackend.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.hfut.newsbackend.service.inter.OssService;
import com.hfut.newsbackend.utils.OSSClientUtil;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.joda.* ;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/24 19:06
 */
@Service
public class OssServiceImpl implements OssService {
    @Override
    public String publishImgToOSS(MultipartFile file) {
        // 工具类取值
        String endPoint = OSSClientUtil.END_POINT;
        String accessKeyId = OSSClientUtil.ACCESS_KEY_ID;
        String accessKeySecret = OSSClientUtil.ACCESS_KEY_SECRET;
        String bucketName = OSSClientUtil.BUCKET_NAME;
        try {
            // 创建OSS实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
            // 上传文件流
            InputStream inputStream = file.getInputStream();
            // 获取文件名称
            String fileName = file.getOriginalFilename();
            // 1 在文件名称里面添加随机唯一的值 防止重名
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
            // 2 把文件按照日期进行分类
            String dataPath = new DateTime().toString("yyyy/MM");
            // 3 拼接
            fileName = "avator/" + dataPath + "/" + fileName;
            // 4 调用oss方法实现上传
            ossClient.putObject(bucketName, fileName, inputStream);
            // 5 关闭ossClient
            ossClient.shutdown();
            // 把上传之后的文件路径返回  需要符合阿里云oss的上传路径
            // https://nsx-fitness.oss-cn-guangzhou.aliyuncs.com/..
            String url = "https://" + bucketName + "." + endPoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

