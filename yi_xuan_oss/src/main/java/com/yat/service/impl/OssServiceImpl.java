package com.yat.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.yat.component.OssCallbackParam;
import com.yat.component.OssCallbackResult;
import com.yat.component.OssPolicyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/19 - 18:03
 */
@Slf4j
@Service("ossService")
@RequiredArgsConstructor
public class OssServiceImpl {

    @Value("${aliyun.oss.policy.expire}")
    private int aliyunOssExpire;
    @Value(("${aliyun.oss.maxSize}"))
    private int aliyunOssMaxsize;
    @Value("${aliyun.oss.callback}")
    private String aliyunOssCallback;
    @Value("${aliyun.oss.bucketName}")
    private String aliyunOssBucketName;
    @Value("${aliyun.oss.endpoint}")
    private String aliyunOssEndpoint;
    @Value("${aliyun.oss.dir.prefix}")
    private String aliyunOssDirPrefix;

    private final OSSClient ossClient;

    /**
     * oss 上传生成策略
     *
     * @return 、
     */
    public OssPolicyResult policy() {
        OssPolicyResult ossPolicyResult = new OssPolicyResult();
        // 存储目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = aliyunOssDirPrefix + sdf.format(new Date());
        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + aliyunOssExpire * 1000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        int maxSize = aliyunOssMaxsize * 1024 * 1024;
        // 回调
        OssCallbackParam ossCallbackParam = new OssCallbackParam();
        ossCallbackParam.setCallbackUrl(aliyunOssCallback);
        ossCallbackParam.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        ossCallbackParam.setCallbackBodyType("application/x-www-form-urlencoded");
        // 提交节点
        String action = "http://" + aliyunOssEndpoint;
        try {
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            String callbackData = BinaryUtil.toBase64String(JSONUtil.parse(ossCallbackParam).toString().getBytes(StandardCharsets.UTF_8));
            // 返回结果
            ossPolicyResult.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
            ossPolicyResult.setPolicy(policy);
            ossPolicyResult.setSignature(signature);
            ossPolicyResult.setDir(dir);
            ossPolicyResult.setCallback(callbackData);
            ossPolicyResult.setHost(action);
        } catch (Exception e) {
            log.error("签名生成失败", e);
        }
        return ossPolicyResult;
    }

    /**
     * oss上传成功回调
     *
     * @param request 、
     * @return 、
     */
    public OssCallbackResult callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = new OssCallbackResult();
        String filename = request.getParameter("filename");
        filename = "http://".concat(aliyunOssBucketName).concat(".").concat(aliyunOssEndpoint).concat("/").concat(filename);
        ossCallbackResult.setFilename(filename);
        ossCallbackResult.setSize(request.getParameter("size"));
        ossCallbackResult.setMimeType(request.getParameter("mimeType"));
        ossCallbackResult.setWidth(request.getParameter("width"));
        ossCallbackResult.setHeight(request.getParameter("height"));
        return ossCallbackResult;
    }
}
