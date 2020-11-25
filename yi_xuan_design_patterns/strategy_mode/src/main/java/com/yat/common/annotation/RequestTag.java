package com.yat.common.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: 自定义策略注解 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/25 - 9:59
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestTag {

    /**
     * 支付方式
     */
    String value() default "";

}
