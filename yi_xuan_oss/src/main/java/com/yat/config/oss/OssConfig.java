package com.yat.config.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 用于配置OSS的连接客户端OSSClient。 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/19 17:54
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig {

    /**
     *
     */
    private String endpoint;
    /**
     *
     */
    private String accessKeyId;
    /**
     *
     */
    private String secretAccessKey;

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
    @Bean
    public OSSClient ossClient() {
        return new OSSClient(endpoint,new DefaultCredentialProvider(accessKeyId, secretAccessKey), null);
    }
}
