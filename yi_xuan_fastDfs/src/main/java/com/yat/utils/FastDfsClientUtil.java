package com.yat.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yat.utils.image.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FastDfsClientUtil {


    @Value("${fdfs.reqHost}")
    private String reqHost;

    @Value("${fdfs.reqPort}")
    private String reqPort;

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 创建缩略图
     */
    @Autowired
    private ThumbImageConfig thumbImageConfig;


    /**
     * 上传图片，并生成缩略图
     *
     * @param file 图片文件
     * @return
     * @throws IOException
     */
    public String uploadFileImage(MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getContentType());
        if (ImageUtil.isImageByFileName(file.getOriginalFilename())) {
            StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()), null);

            String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
            log.info("略缩图：thumbImage--->{}", "http://" + reqHost + ":" + reqPort + "/group1" + path);
            return getResAccessUrl(storePath);
        }
        return "请上传正确的图片";
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return getResAccessUrl(storePath);
    }

    public void delFile(String filePath) {
        storageClient.deleteFile(filePath);

    }


    public InputStream download(String groupName, String path) {
        // 将此ins返回给上面的ins
        InputStream ins = storageClient.downloadFile(groupName, path, FastDfsClientUtil::recv);
        return ins;
    }

    private static InputStream recv(InputStream ins1) {
        return ins1;
    }

    /**
     * 封装文件完整URL地址
     *
     * @param storePath
     * @return
     */
    private String getResAccessUrl(StorePath storePath) {
        return "http://" + reqHost + ":" + reqPort + "/" + storePath.getFullPath();
    }

}
