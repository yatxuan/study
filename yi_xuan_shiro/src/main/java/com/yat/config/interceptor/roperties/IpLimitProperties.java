package com.yat.config.interceptor.roperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: IP限流配置 </p>
 * @author Yat-Xuan
 * @date 2020/3/13 16:53
*/
@Data
@Configuration
@ConfigurationProperties(prefix = "yat.ip.second.limit")
public class IpLimitProperties {

    /**
     * 是否开启IP限流
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
