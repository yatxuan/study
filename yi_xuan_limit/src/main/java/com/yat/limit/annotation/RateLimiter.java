package com.yat.limit.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author Yat
 */
@Target(ElementType.METHOD)//作用于方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 限流次数，默认五次
     *
     * @return 、
     */
    int limit() default 5;

    /**
     * 限流时间，单位为秒，默认一分钟
     *
     * @return 、
     */
    int timeout() default 60;

    /**
     * 是否为全局限流，true-全局  false-ip
     * @return 、
     */
    boolean isGlobal() default true;
}
