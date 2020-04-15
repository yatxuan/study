package com.yat.minio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.minio.ObjectStat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/14 13:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinioObject {

    /**
     * 桶
     */
    private String bucketName;
    /**
     * 上传的文件名称
     */
    private String name;
    /**
     * 上传文件的时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date createdTime;
    /**
     * 文件大小
     */
    private long length;
    private String etag;
    /**
     * 文件类型
     */
    private String contentType;
    private String path;

    public MinioObject(ObjectStat os) {
        this.bucketName = os.bucketName();
        this.name = os.name();
        this.createdTime = os.createdTime();
        this.length = os.length();
        this.etag = os.etag();
        this.contentType = os.contentType();
    }

    public MinioObject(ObjectStat os, String endpoint) {
        this.bucketName = os.bucketName();
        this.name = os.name();
        this.createdTime = os.createdTime();
        this.length = os.length();
        this.etag = os.etag();
        this.contentType = os.contentType();
        this.path = String.format("%s/%s/%s", endpoint, this.bucketName, this.name);
    }

}
