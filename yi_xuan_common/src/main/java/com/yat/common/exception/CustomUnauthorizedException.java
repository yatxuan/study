package com.yat.common.exception;

/**
 * <p>Description: 自定义无权限异常 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2019/9/9
 * @Time: 11:36
 */
public class CustomUnauthorizedException extends RuntimeException {

    public CustomUnauthorizedException(String msg){
        super(msg);
    }

    public CustomUnauthorizedException() {
        super();
    }
}

