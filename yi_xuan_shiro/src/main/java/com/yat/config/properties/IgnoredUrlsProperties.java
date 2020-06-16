package com.yat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 配置不拦截的URL </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/13 16:52
 */
@Data
@Component
@ConfigurationProperties(prefix = "ignored")
public class IgnoredUrlsProperties {
    /**
     * 无限流及黑名单不拦截的路径
     */
    private List<String> limitUrls = new ArrayList<>();
    /**
     * 无需登陆的请求
     */
    private List<String> urls = new ArrayList<>();
}
