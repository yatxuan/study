package com.yat.config.strategy;

import com.yat.utils.fastdfs.FastDfsRequest;
import com.yat.utils.fastdfs.FastDfsResponse;

/**
 * <p>Description: 定义总策略 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/8/24 - 20:26
 */
public interface BaseUploadStrategy {

    /**
     * 文件上传
     *
     * @param fastDfsRequest  、
     * @param fastDfsResponse 、
     * @return 、
     */
    FastDfsResponse fileUpdate(FastDfsRequest fastDfsRequest, FastDfsResponse fastDfsResponse);
}
