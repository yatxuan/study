package com.yat.utils.fastdfs;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.yat.constant.FileType;
import com.yat.utils.image.ImageUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Set;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/8/24 - 17:57
 */
@Slf4j
@Data
@Builder
public class FastDfsRequest {

    /**
     * 文件流
     */
    private InputStream fileInputStream;

    /**
     * 文件的大小
     */
    private long size;

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 是否为图片
     */
    private boolean isImage;
    /**
     * 是否生成略缩图
     */
    private boolean isThumbnails;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 上传日期
     */
    private Date uploadDate;
    /**
     * 文件元数据
     */
    private Set<MetaData> metaDataSet;
    /**
     * 文件上传类型
     */
    private int fileUploadType;

    /**
     * 文件数据封装
     *
     * @param file 文件数据
     * @return 、
     */
    public static FastDfsRequest init(MultipartFile file) {
        return init(file, false);
    }

    /**
     * 文件数据封装
     *
     * @param file         文件数据
     * @param isThumbnails 是否生成略缩图
     * @return 、
     */
    public static FastDfsRequest init(MultipartFile file, boolean isThumbnails) {
        try {
            FastDfsRequest fastDfsRequest = FastDfsRequest.builder()
                    .fileInputStream(file.getInputStream())
                    .size(file.getSize())
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .uploadDate(new Date())
                    .fileUploadType(FileType.UPLOAD_ORDINARY_FILE)
                    .build();
            if (ImageUtil.isImageByFileName(fastDfsRequest.getFileName())) {
                fastDfsRequest.setFileUploadType(isThumbnails ? FileType.UPLOAD_IMG_FILE_YSE : FileType.UPLOAD_IMG_FILE_NO);
                fastDfsRequest.setThumbnails(isThumbnails);
                fastDfsRequest.setImage(true);
            }


            return fastDfsRequest;
        } catch (IOException e) {
            log.error("获取文件流失败--->'{}'", e.getMessage());
            return null;
        }
    }
}
