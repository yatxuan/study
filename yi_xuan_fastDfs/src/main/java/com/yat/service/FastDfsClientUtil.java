package com.yat.service;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yat.config.strategy.HandlerUploadContext;
import com.yat.utils.fastdfs.FastDfsRequest;
import com.yat.utils.fastdfs.FastDfsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/5
 * @Time: 14:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FastDfsClientUtil {

    private final FastFileStorageClient storageClient;
    private final HandlerUploadContext handlerUploadContext;

    /**
     * 获取文件信息
     *
     * @param filePath 文件路径：http://accessimage.yatxuan.cn/group1/M00/00/00/rBKsVl6Gp7WAeOgJAAAQmDrLZTA452.jpg
     */
    public FileInfo queryFileInfo(String filePath) {
        StorePath storePath = StorePath.parseFromUrl(filePath);
        return storageClient.queryFileInfo(storePath.getGroup(), storePath.getPath());
    }
    /**
     * 上传文件
     *
     * @param file 文件数据
     * @param isThumbnails 是否生成略缩图
     * @return 、
     */
    public FastDfsResponse uploadFile(MultipartFile file, boolean isThumbnails) {
        FastDfsRequest init = FastDfsRequest.init(file, isThumbnails);
        if (init == null) {
            log.error("文件上传失败！！！！！！！！！");
            return null;
        }
        return uploadFile(init);
    }

    /**
     * 上传文件
     *
     * @param fastDfsRequest 文件数据
     * @return 、
     */
    private FastDfsResponse uploadFile(FastDfsRequest fastDfsRequest) {
        FastDfsResponse fastDfsResponse = new FastDfsResponse(fastDfsRequest);
        return handlerUploadContext.getOrderStrategy(fastDfsRequest.getFileUploadType())
                .fileUpdate(fastDfsRequest, fastDfsResponse);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径：http://accessimage.yatxuan.cn/group1/M00/00/00/rBKsVl6Gp7WAeOgJAAAQmDrLZTA452.jpg
     */
    public void delFile(String filePath) {
        storageClient.deleteFile(filePath);
    }


    /**
     * 下载文件
     *
     * @param groupName 、
     * @param path      、
     * @return 、
     */
    public InputStream download(String groupName, String path) {
        // 将此ins返回给上面的ins
        return storageClient.downloadFile(groupName, path, FastDfsClientUtil::recv);
    }

    private static InputStream recv(InputStream ins1) {
        return ins1;
    }


}
