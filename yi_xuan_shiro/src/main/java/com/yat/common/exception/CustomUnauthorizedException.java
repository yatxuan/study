package com.yat.common.exception;

import lombok.NoArgsConstructor;

/**
 * <p>Description: 自定义无权限异常 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 14:19
 */
@NoArgsConstructor
public class CustomUnauthorizedException extends RuntimeException {

    public CustomUnauthorizedException(String msg) {
        super(msg);
    }
}
