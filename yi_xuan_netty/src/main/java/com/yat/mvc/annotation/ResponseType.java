package com.yat.mvc.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: 类似ResponseBody </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:24
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseType {

    String value() default "JSON";
}
