package com.yat.minio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.Data;

import java.util.Date;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/14 16:02
 */
@Data
public class MinioItem {

    /**
     * 桶
     */
    private String bucketName;
    /**
     * 上传的文件名称
     */
    private String objectName;
    /**
     * 上传文件的时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date lastModified;
    private String etag;
    /**
     * 文件大小 KB
     */
    private long size;
    private String storageClass;
    private Owner owner;
    private String type;

    public MinioItem(String objectName, Date lastModified, String etag, long size, String storageClass, Owner owner, String type) {
        this.objectName = objectName;
        this.lastModified = lastModified;
        this.etag = etag;
        this.size = size;
        this.storageClass = storageClass;
        this.owner = owner;
        this.type = type;
    }


    public MinioItem(Item item) {
        this.objectName = item.objectName();
        this.lastModified = item.lastModified();
        this.etag = item.etag();
        this.size = item.size();
        this.storageClass = item.storageClass();
        this.owner = item.owner();
        this.type = item.isDir() ? "directory" : "file";
    }

}
