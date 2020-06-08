package com.yat.limit.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 配置不拦截的URL </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/13 16:52
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ignored")
public class IgnoredUrlsProperties {
    /**
     * 无限流及黑名单不拦截的路径
     */
    private List<String> limitUrls = new ArrayList<>();
}
