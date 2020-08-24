package com.yat.config.strategy;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yat.annotation.HandlerFileUploadType;
import com.yat.constant.FileType;
import com.yat.utils.fastdfs.FastDfsRequest;
import com.yat.utils.fastdfs.FastDfsResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 上传普通文件 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/8/24 - 20:43
 */
@Component
@RequiredArgsConstructor
@HandlerFileUploadType(FileType.UPLOAD_ORDINARY_FILE)
public class UploadOrdinaryFileStrategy implements BaseUploadStrategy {

    @Value("${fdfs.reqHost}")
    private String reqHost;

    @Value("${fdfs.reqPort}")
    private String reqPort;


    private final FastFileStorageClient storageClient;

    @Override
    public FastDfsResponse fileUpdate(FastDfsRequest fastDfsRequest, FastDfsResponse fastDfsResponse) {
        StorePath storePath = storageClient.uploadFile(fastDfsRequest.getFileInputStream(),
                fastDfsRequest.getSize(),
                FilenameUtils.getExtension(fastDfsRequest.getFileName()),
                null
        );
        fastDfsResponse.setFileUrl(getFilePath(storePath));
        return fastDfsResponse;
    }


    /**
     * 获取文件的完整访问路径
     *
     * @param storePath 、
     * @return 、
     */
    private String getFilePath(StorePath storePath) {
        return getResAccessUrl(storePath.getFullPath());
    }

    /**
     * 封装文件完整URL地址
     *
     * @param path /
     * @return 访问图片的 URL
     */
    private String getResAccessUrl(String path) {
        return "http://" + reqHost + ":" + reqPort + "/" + path;
    }

}
