package com.yat.social.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 自定义异常 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:08
 */
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private Integer code = 500;

    private String message;


    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
