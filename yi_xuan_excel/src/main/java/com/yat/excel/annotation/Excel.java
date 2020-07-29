package com.yat.excel.annotation;

import java.lang.annotation.*;

/**
 * <p>Description: Excel字段注解 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Excel {

    /**
     * Excel中表头字段
     */
    String title();
}
