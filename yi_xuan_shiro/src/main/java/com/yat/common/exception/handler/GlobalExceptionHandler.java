package com.yat.common.exception.handler;

import com.yat.common.exception.BadRequestException;
import com.yat.common.exception.CustomException;
import com.yat.common.exception.CustomUnauthorizedException;
import com.yat.common.exception.ThrowableUtil;
import com.yat.common.utils.ResultResponse;
import com.yat.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
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
    @ExceptionHandler(
            value = {
                    Throwable.class,
                    Exception.class
            }
    )
    public Object handleException(HttpServletRequest request, Throwable e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        ResultResponse response = ResultResponse.error(HttpStatus.BAD_REQUEST.value(), "未知异常，请联系管理员");
        return getResultResponse(request, response, e);
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常 - 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     * 单独捕捉Shiro(CustomUnauthorizedException)异常 - 该异常为自定义无权限异常
     * 单独捕捉Shiro(UnauthorizedException)异常 - 权限不足,该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     * 单独捕捉Shiro(ShiroException)异常 - 捕捉所有Shiro异常
     */
    @ExceptionHandler(
            value = {
                    UnauthenticatedException.class,
                    CustomUnauthorizedException.class,
                    UnauthorizedException.class,
                    ShiroException.class
            }
    )
    public Object handle401(HttpServletRequest request, UnauthenticatedException e) {
        log.error("无权访问(Unauthorized):当前Subject是匿名Subject，请先登录");
        log.error(ThrowableUtil.getStackTrace(e));

        String message = e.getMessage();
        if (StringUtils.isBlank(message)) {
            message = "无权访问，请先登录";
        }
        ResultResponse response = ResultResponse.error(HttpStatus.UNAUTHORIZED.value(), message);
        return getResultResponse(request, response, e);
    }

    /**
     * 捕捉校验异常(BindException)
     */
    @ExceptionHandler(BindException.class)
    public Object validException(HttpServletRequest request, BindException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        ResultResponse response = ResultResponse.error(HttpStatus.BAD_REQUEST.value(), "参数列表错误");
        return getResultResponse(request, response, e);
    }


    /**
     * 捕捉404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handle(HttpServletRequest request, NoHandlerFoundException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        ResultResponse response = ResultResponse.error(HttpStatus.NOT_FOUND.value(), "资源，服务未找到");
        return getResultResponse(request, response, e);
    }


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = CustomException.class)
    public Object badRequestException(HttpServletRequest request, BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        ResultResponse response = ResultResponse.error(e.getStatus(), e.getMessage());
        return getResultResponse(request, response, e);
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        String[] str = Objects.requireNonNull(
                e.getBindingResult().getAllErrors().get(0).getCodes()
        )[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) {
            message = str[1] + ":" + message;
        }
        ResultResponse response = ResultResponse.error(HttpStatus.BAD_REQUEST.value(), message);
        return getResultResponse(request, response, e);
    }

    /**
     * 判断是否是ajax请求
     */
    private boolean isAjax(HttpServletRequest httpRequest) {
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        return ("XMLHttpRequest".equals(xRequestedWith));
    }


    private Object getResultResponse(HttpServletRequest request, ResultResponse resultResponse, Throwable e) {
        if (isAjax(request)) {
            return resultResponse;
        } else {
            ModelAndView mav = new ModelAndView();
            // 传入对象
            mav.addObject("exception", e);
            mav.addObject("url", request.getRequestURL());
            // 要跳转的页面
            mav.setViewName("/404.html");
            return mav;
        }
    }
}
