package com.yat.common.exception.user;

/**
 * <p>Description: 验证码错误异常类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 11:04
 */
public class CaptchaException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("user.jcaptcha.error", null);
    }
}
