package com.yat.minio.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/14
 * @Time: 10:14
 */
public class MinioConfig {

    private static volatile MinioClient minioClient = null;

    /**
     * minio服务的IP端口
     */
    private final static String URL = "http://minio.yatxuan.cn";
    /**
     * 账号
     */
    private final static String ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    /**
     * 密码
     */
    private final static String SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";


    /**
     * 私有化构造方法
     */
    private MinioConfig() {
    }

    public static MinioClient getInstance() throws InvalidPortException, InvalidEndpointException {
        //第一次校验
        if (null == minioClient) {
            synchronized (MinioClient.class) {
                //第二次校验
                if (null == minioClient) {
                    minioClient = new MinioClient(URL, ACCESS_KEY, SECRET_KEY, false);
                }
            }
        }
        return minioClient;
    }
}
