package com.yat.limit.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 全局限流配置 </p>
 * @author Yat-Xuan
 * @date 2020/3/13 16:53
*/
@Data
@Configuration
@ConfigurationProperties(prefix = "yat.rate.limit")
public class LimitProperties {

    /**
     * 是否开启全局限流
     */
    private Boolean enable = false;

    /**
     * 限制请求个数
     */
    private Integer limit = 100;

    /**
     * 每单位时间内（毫秒）
     */
    private Integer timeout = 1000;
}
