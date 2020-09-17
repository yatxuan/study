package com.yat.mvc.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: 类似RequestMapping </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:23
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionMapping {

	String actionKey() default "";

	String httpMethod() default "";
}
