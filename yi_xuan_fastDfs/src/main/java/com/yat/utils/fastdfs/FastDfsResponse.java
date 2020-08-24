package com.yat.utils.fastdfs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yat.utils.image.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FastDfsResponse implements Serializable {

    private static final long serialVersionUID = 3698477140179228909L;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadDate;


    public FastDfsResponse(FastDfsRequest fastDfsRequest) {
        this.code = 200;
        this.fileName = fastDfsRequest.getFileName();
        this.fileSuffix = StringUtils.substring(this.fileName, StringUtils.indexOf(this.fileName, ".") + 1);
        this.uploadDate = new Date();
        if (fastDfsRequest.getSize() > thousand) {
            this.fileUnit = "KB";
            this.fileSize = fastDfsRequest.getSize() / thousand;
            if (this.fileSize > thousand) {
                this.fileUnit = "MB";
                this.fileSize = this.fileSize / thousand;
            }
        } else {
            this.fileUnit = "Byte";
            this.fileSize = fastDfsRequest.getSize();
        }

        if (ImageUtil.isImageByFileName(this.getFileName())) {
            this.fileType = fastDfsRequest.getFileType();
            this.isThumbnails = fastDfsRequest.isThumbnails();
        } else {
            this.fileType = this.fileSuffix;
        }

    }
}
