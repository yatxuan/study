package com.yat.social.common.exception;

import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <p>Description: 异常处理类 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/19 - 2:10
 */
@Slf4j
@RestControllerAdvice
public class OauthExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public AuthResponse customExceededError(CustomException ex) {
        log.error("异常数据：------------->{}", ex.getMessage());
        return AuthResponse.builder()
                .code(ex.getCode())
                .msg(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = {AuthException.class})
    public AuthResponse authExceededError(AuthException ex) {
        log.error("异常数据：------------->{}", ex.getMessage());
        return AuthResponse.builder()
                .code(500)
                .msg("系统内部错误，参数不完整")
                .build();
    }

}
