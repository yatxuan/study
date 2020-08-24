package com.yat.constant;

/**
 * <p>Description:   if-else的条件 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/8/24 - 20:35
 */
public interface FileType {

    /**
     * 上传普通文件
     */
    int UPLOAD_ORDINARY_FILE = 1;
    /**
     * 上传图片文件,并且不生成略缩图
     */
    int UPLOAD_IMG_FILE_NO = 2;
    /**
     * 上传图片文件,并且生成略缩图
     */
    int UPLOAD_IMG_FILE_YSE = 3;
}
