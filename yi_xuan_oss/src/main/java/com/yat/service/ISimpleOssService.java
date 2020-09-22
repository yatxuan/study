package com.yat.service;

/**
 * <p>Description: 简单上传 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/19 - 19:45
 */
public interface ISimpleOssService {

    /**
     * 流式上传
     */
    void streamingUpload();

    /**
     * 上传Byte数组
     */
    void byteUpload();

    /**
     * 上传网络流
     */
    void networkFlowUpload();

    /**
     * 上传文件流
     */
    void fileStreamUpload();

    /**
     * 上传本地文件
     * @return 访问文件的URL
     */
    String localFilesUpload();
}
