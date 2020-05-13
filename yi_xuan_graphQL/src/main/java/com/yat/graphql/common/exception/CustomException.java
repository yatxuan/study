package com.yat.graphql.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Integer code;

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
