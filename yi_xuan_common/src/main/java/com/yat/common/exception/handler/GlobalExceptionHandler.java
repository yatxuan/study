package com.yat.common.exception.handler;

import com.yat.common.exception.BadRequestException;
import com.yat.common.exception.CustomException;
import com.yat.common.exception.CustomUnauthorizedException;
import com.yat.common.exception.ThrowableUtil;
import com.yat.common.exception.job.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiError> handleException(Throwable e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    /**
     * 捕捉所有Shiro异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResponseEntity<ApiError> handle401(ShiroException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):" + e.getMessage()));
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常 - 权限不足
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handle401(UnauthorizedException e) {
        return buildResponseEntity(ApiError.error(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):当前Subject没有此请求所需权限(" + e.getMessage() + ")"));
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiError> handle401(UnauthenticatedException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):当前Subject是匿名Subject，请先登录(This subject is anonymous.)"));
    }

    /**
     * 捕捉UnauthorizedException自定义异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomUnauthorizedException.class)
    public ResponseEntity<ApiError> handle401(CustomUnauthorizedException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):" + e.getMessage()));
    }

    /**
     * 捕捉校验异常(BindException)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> validException(BindException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }


    /**
     * 捕捉404异常
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handle(NoHandlerFoundException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    /**
     * 捕捉其他所有异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> globalException(Throwable e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = TaskException.class)
    public ResponseEntity<ApiError> taskExistException(TaskException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    /**
     * 处理 BadRequestException
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ApiError> badRequestExistException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ApiError> entityExistException(CustomException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) {
            message = str[1] + ":" + message;
        }
        return buildResponseEntity(ApiError.error(message));
    }

    /**
     * 统一返回
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
