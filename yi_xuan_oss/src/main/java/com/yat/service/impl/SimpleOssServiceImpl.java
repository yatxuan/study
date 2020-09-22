package com.yat.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.yat.config.oss.OssConfig;
import com.yat.service.IOssService;
import com.yat.service.ISimpleOssService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/19 - 19:51
 */
@Slf4j
@Service("simpleOssService")
@RequiredArgsConstructor
public class SimpleOssServiceImpl implements ISimpleOssService, IOssService {

    private final OSSClient ossClient;
    private final OssConfig ossConfig;


    @Override
    public String generatePresignedUrl(String bucketName, String key, Date expiration) {
        return ossClient.generatePresignedUrl(bucketName, key, expiration).toString();
    }

    @Override
    public void streamingUpload() {

    }

    @Override
    public void byteUpload() {

    }

    @Override
    public void networkFlowUpload() {

    }

    @Override
    public void fileStreamUpload() {

    }

    @Override
    public String localFilesUpload() {

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getAliyunOssBucketName(), ossConfig.getAliyunOssDirPrefix() + "test.jpg", new File("D:\\img/1582787548492.jpg"));
        // 上传文件。
        PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
        JSON parse = JSONUtil.parseObj(putObjectResult, false);
        System.out.println(parse.toStringPretty());

        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        return generatePresignedUrl(ossConfig.getAliyunOssBucketName(), ossConfig.getAliyunOssDirPrefix() + "test.jpg", expiration);
    }

}
