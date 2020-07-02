package com.yat.common.exception;


import com.yat.common.constant.HttpStatus;

/**
 * <p>Description: 自定义异常 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:08
 */
public class CustomException extends RuntimeException {

    private Integer code = HttpStatus.ACCEPTED;
    private String message;

    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public CustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
