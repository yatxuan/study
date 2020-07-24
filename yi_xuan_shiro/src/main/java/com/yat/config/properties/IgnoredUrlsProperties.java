package com.yat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 这里配置的是不被Shiro拦截的URL </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/13 16:52
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ignored")
public class IgnoredUrlsProperties {

    /**
     * 无需登录认证的请求
     */
    private List<String> urls = new ArrayList<>();
}
