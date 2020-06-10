package com.yat.limit.config;

import com.yat.limit.annotation.RateLimiter;
import com.yat.limit.config.properties.IpLimitProperties;
import com.yat.limit.config.properties.LimitProperties;
import com.yat.limit.exception.BadRequestException;
import com.yat.limit.service.IRosterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * <p>Description: 限流拦截器:该拦截器配置好后，需要放在SpringMVC的拦截器中，才会启用 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/13 17:56
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LimitRaterInterceptor extends HandlerInterceptorAdapter {

    private final LimitProperties limitProperties;
    private final IpLimitProperties ipLimitProperties;
    private final RedisRaterLimiter redisRaterLimiter;
    private final IRosterService rosterService;

    /**
     * 限流标识
     */
    private final static String LIMIT_ALL = "YAT_LIMIT_ALL";

    /**
     * 预处理回调方法，实现处理器的预处理（如登录检查）
     * 第三个参数为响应的处理器，即controller
     * 返回true，表示继续流程，调用下一个拦截器或者处理器
     * 返回false，表示流程中断，通过response产生响应
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {

        // 跨域配置
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");

        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }


        String ip = AddressUtils.getIpAddr(request);
        // 获取浏览器信息
        String browser = AddressUtils.getBrowser(request);
        // 获取详细地址
        String cityInfo = AddressUtils.getCityInfo(ip);
        // 判断当前ip是否为黑名单数据
        if (rosterService.isBlackPresence(ip)) {
            log.error("当前ip'{}'为黑名单ip，禁止访问,浏览器为：'{}',消息地址为：'{}'", ip, browser, cityInfo);
            throw new BadRequestException("禁止访问");
        }

        // 判断当前ip是否为白名单数据,ip白名单不进行限速
        if (!rosterService.isWhitePresence(ip)) {
            // ip限流
            if (ipLimitProperties.getEnable()) {
                boolean ipToken = redisRaterLimiter.acquireToken(ip,
                        ipLimitProperties.getLimit(), ipLimitProperties.getTimeout());
                if (ipToken) {
                    log.error("当前IP:{}访问速度过快！浏览器为：'{}',消息地址为：'{}'！！！！！！！！！！！！！", ip, browser, cityInfo);
                    throw new BadRequestException("你手速怎么这么快，请点慢一点");
                }
            }
        }

        // 全局限流
        if (limitProperties.getEnable()) {
            boolean allToken = redisRaterLimiter.acquireToken(LIMIT_ALL,
                    limitProperties.getLimit(), limitProperties.getTimeout());
            if (allToken) {
                log.error("当前访问人数过多！！！！！！！！！！！！！！");
                throw new BadRequestException("当前访问总人数太多啦，请稍后再试");
            }
        }

        // 注解限流
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
                if (rateLimiter != null) {
                    int limit = rateLimiter.limit();
                    int timeout = rateLimiter.timeout();
                    boolean rateToken = redisRaterLimiter.acquireToken(method.getName(), limit, timeout);
                    if (rateToken) {
                        log.error("该方法:{}当前访问人数太多，！！！！！！！！！！！！！！", method.getName());
                        throw new BadRequestException("当前访问人数太多啦，请稍后再试");
                    }
                }
            }

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }

        return true;
    }

    /**
     * 当前请求进行处理之后，也就是Controller方法调用之后执行，
     * 但是它会在DispatcherServlet 进行视图返回渲染之前被调用。
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
    }

    /**
     * 方法将在整个请求结束之后，也就是在DispatcherServlet渲染了对应的视图之后执行。
     * 这个方法的主要作用是用于进行资源清理工作的。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
    }

}
