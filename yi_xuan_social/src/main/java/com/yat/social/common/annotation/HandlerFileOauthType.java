package com.yat.social.common.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 自定义策略注解
 * </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/24 20:35
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HandlerFileOauthType {
    /**
     * 策略类型
     */
    String value();
}
