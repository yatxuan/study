package com.yat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 这里配置的是不被 'SpringMVC' 的Web拦截器拦截的URL</p>
 * @author Yat-Xuan
 * @date 2020/3/13 16:52
*/
@Data
@Configuration
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    /**
     * 图片接口
     */
    private List<String> image = new ArrayList<>();

    /**
     * 短信接口
     */
    private List<String> sms = new ArrayList<>();

    /**
     * 邮箱接口
     */
    private List<String> email = new ArrayList<>();
}
