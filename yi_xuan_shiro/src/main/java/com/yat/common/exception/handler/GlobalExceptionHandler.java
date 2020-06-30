package com.yat.common.exception.handler;

import com.yat.common.exception.BadRequestException;
import com.yat.common.exception.CustomException;
import com.yat.common.exception.CustomUnauthorizedException;
import com.yat.common.exception.ThrowableUtil;
import com.yat.common.utils.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

/**
 * <p>Description: Shiro异常处理 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/23 12:48
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public ResultResponse handleException(Throwable e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(HttpStatus.BAD_REQUEST.value(), "未知异常，请联系管理员");
    }

    /**
     * 捕捉所有Shiro异常
     */
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "无权限访问")
    @ExceptionHandler(ShiroException.class)
    public ResultResponse handle401(ShiroException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(HttpStatus.UNAUTHORIZED.value(), "无权访问");
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常 - 权限不足
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     */
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "权限不足")
    @ExceptionHandler(UnauthorizedException.class)
    public ResultResponse handle401(UnauthorizedException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(HttpStatus.UNAUTHORIZED.value(),
                "权限不足");
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     */
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "无权访问，请先登录")
    @ExceptionHandler(UnauthenticatedException.class)
    public ResultResponse handle401(UnauthenticatedException e) {
        log.error("无权访问(Unauthorized):当前Subject是匿名Subject，请先登录");
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(HttpStatus.UNAUTHORIZED.value(),
                "无权访问，请先登录");
    }

    /**
     * 捕捉UnauthorizedException自定义异常
     */
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "无权限访问")
    @ExceptionHandler(CustomUnauthorizedException.class)
    public ResultResponse handle401(CustomUnauthorizedException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(HttpStatus.UNAUTHORIZED.value(), "无权访问");
    }

    /**
     * 捕捉校验异常(BindException)
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "参数列表错误")
    @ExceptionHandler(BindException.class)
    public ResultResponse validException(BindException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(HttpStatus.BAD_REQUEST.value(), "参数列表错误");
    }


    /**
     * 捕捉404异常
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "资源，服务未找到")
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultResponse handle(NoHandlerFoundException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(HttpStatus.NOT_FOUND.value(), "资源，服务未找到");
    }

    /**
     * 捕捉其他所有异常
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "未知异常，请联系管理员")
    @ExceptionHandler(Exception.class)
    public ResultResponse globalException(Throwable e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
    }


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResultResponse badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(e.getStatus(), e.getMessage());
    }

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = CustomException.class)
    public ResultResponse entityExistException(CustomException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return ResultResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) {
            message = str[1] + ":" + message;
        }
        return ResultResponse.error(HttpStatus.BAD_REQUEST.value(), message);
    }

}
