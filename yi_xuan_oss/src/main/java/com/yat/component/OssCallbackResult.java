package com.yat.component;

import lombok.Data;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/19 18:01
 */
@Data
public class OssCallbackResult {
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 文件大小
     */
    private String size;
    /**
     * 文件的mimeType
     */
    private String mimeType;
    /**
     * 图片文件的宽
     */
    private String width;
    /**
     * 图片文件的高
     */
    private String height;
}
