package com.yat.mvc.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:23
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionBody {
    boolean required() default true;
}
