package com.yat.service;

import com.yat.component.OssCallbackResult;
import com.yat.component.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>Description: oss上传管理Service </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/19 18:02
 */
public interface IOssService {

    /**
     * 简单上传
     *
     * @return 、
     */
    String generatePresignedUrl(String bucketName, String key, Date expiration);
}
