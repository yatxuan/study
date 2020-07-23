package com.yat.common.exception.file;

import com.yat.common.exception.BaseException;

/**
 * <p>Description: 文件信息异常类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:02
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
