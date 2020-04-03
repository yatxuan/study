package com.yat.common.exception.user;

/**
 * <p>Description: 验证码失效异常类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:04
 */
public class CaptchaExpireException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
