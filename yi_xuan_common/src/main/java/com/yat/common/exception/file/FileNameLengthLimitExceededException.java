package com.yat.common.exception.file;

/**
 * <p>Description: 文件名称超长限制异常类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:03
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
