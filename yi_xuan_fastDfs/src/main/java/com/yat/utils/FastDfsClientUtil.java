package com.yat.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yat.utils.image.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    @Value("${fdfs.reqHost}")
    private String reqHost;

    @Value("${fdfs.reqPort}")
    private String reqPort;

    private final FastFileStorageClient storageClient;
    /**
     * 创建缩略图
     */
    private final ThumbImageConfig thumbImageConfig;


    /**
     * 上传图片，并生成缩略图
     *
     * @param file 图片文件
     * @return 、
     * @throws IOException 、
     */
    public String uploadFileImage(MultipartFile file) throws IOException {
        // 获取文件名称
        String originalFilename = file.getOriginalFilename();
        log.info(originalFilename);
        log.info(file.getContentType());
        if (ImageUtil.isImageByFileName(originalFilename)) {
            StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                    FilenameUtils.getExtension(originalFilename), null);

            // 获取略缩图
            String thumbnailsUrl = getThumbnailsUrl(storePath);
            log.info("略缩图：thumbImage--->{}", thumbnailsUrl);

            return getFilePath(storePath);
        }
        return "请上传正确的图片";
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 、
     * @throws IOException 、
     */
    public FastDfsResponse uploadFile(MultipartFile file) throws IOException {
        StorePath storePath;
        FastDfsResponse fastDfsResponse = new FastDfsResponse(file);
        if (fastDfsResponse.isThumbnails()) {
            storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                    FilenameUtils.getExtension(fastDfsResponse.getFileName()), null);
            fastDfsResponse.setThumbnailsUrl(getThumbnailsUrl(storePath));
        } else {
            storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()), null);
        }
        fastDfsResponse.setFileUrl(getFilePath(storePath));
        return fastDfsResponse;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径：http://accessimage.yatxuan.cn/group1/M00/00/00/rBKsVl6Gp7WAeOgJAAAQmDrLZTA452.jpg
     */
    public void delFile(String filePath) {
        storageClient.deleteFile(filePath);
    }


    public InputStream download(String groupName, String path) {
        // 将此ins返回给上面的ins
        return storageClient.downloadFile(groupName, path, FastDfsClientUtil::recv);
    }

    private static InputStream recv(InputStream ins1) {
        return ins1;
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
     * 获取略缩图
     *
     * @param storePath /
     * @return 访问略缩图的 URL
     */
    private String getThumbnailsUrl(StorePath storePath) {
        return getResAccessUrl(thumbImageConfig.getThumbImagePath(storePath.getFullPath()));
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
