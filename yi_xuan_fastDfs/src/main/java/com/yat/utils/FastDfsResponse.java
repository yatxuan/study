package com.yat.utils;

import com.yat.utils.image.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/22
 * @Time: 10:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastDfsResponse {

    private final long thousand = 1024L;

    /**
     * code
     */
    private int code;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件大小
     */
    private long fileSize;
    /**
     * 单位
     */
    private String fileUnit;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 是否包含略缩图
     */
    private boolean isThumbnails = false;
    /**
     * 略缩图地址
     */
    private String thumbnailsUrl;
    /**
     * 文件访问路径
     */
    private String fileUrl;
    /**
     * 上传日期
     */
    private Date uploadDate;


    public FastDfsResponse(MultipartFile file) {
        this.code = 200;
        this.fileName = file.getOriginalFilename();
        this.fileSuffix = StringUtils.substring(this.fileName, StringUtils.indexOf(this.fileName, ".") + 1);
        this.uploadDate = new Date();
        if (file.getSize() > thousand) {
            this.fileUnit = "KB";
            this.fileSize = file.getSize() / thousand;
            if (this.fileSize > thousand) {
                this.fileUnit = "MB";
                this.fileSize = this.fileSize / thousand;
            }
        } else {
            this.fileUnit = "Byte";
            this.fileSize = file.getSize();
        }

        if (ImageUtil.isImageByFileName(this.getFileName())) {
            this.fileType = file.getContentType();
            this.isThumbnails = true;
        } else {
            this.fileType = this.fileSuffix;
        }

    }
}
