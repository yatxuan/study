package com.yat.limit.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 配置不被拦截的请求 </p>
 * <p>这里配置的接口用于shiro的配置文件，配置shiro的无状态处理，不拦截,并不是说不限流</p>
 *
 * @author Yat-Xuan
 * @date 2020/3/13 16:52
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    /**
     * 图片获取接口，不会进行限流
     */
    private List<String> image = new ArrayList<>();
    /**
     * 发送短信接口，进行限流操作
     */
    private List<String> sms = new ArrayList<>();
    /**
     * 发送邮件接口，进行限流操作
     */
    private List<String> email = new ArrayList<>();
    /**
     * 无需登录认证的请求，有限流
     */
    private List<String> urls = new ArrayList<>();


}
