package com.yat.common.exception.user;

/**
 * <p>Description: 用户密码不正确或不符合规范异常类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:04
 */
public class UserPasswordNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}
