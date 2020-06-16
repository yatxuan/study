package com.yat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Description: IP限流配置 </p>
 * @author Yat-Xuan
 * @date 2020/3/13 16:53
*/
@Data
@Component
@ConfigurationProperties(prefix = "yat.ip.limit")
public class IpLimitProperties {

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
