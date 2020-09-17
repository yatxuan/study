package com.yat.mvc.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: 忽略请求 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClearAction {
}
