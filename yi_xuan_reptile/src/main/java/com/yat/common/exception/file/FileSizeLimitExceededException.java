package com.yat.common.exception.file;

/**
 * <p>Description: 文件名大小限制异常类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:03
 */
public class FileSizeLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
