package com.yat.common.exception.user;

import com.yat.common.exception.BaseException;

/**
 * <p>Description: 用户信息异常类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:04
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
