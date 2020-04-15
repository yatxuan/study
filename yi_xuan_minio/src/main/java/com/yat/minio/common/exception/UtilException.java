package com.yat.minio.common.exception;

/**
 * <p>Description: 工具类异常 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:10
 */
public class UtilException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
