package com.yat.reptile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Yat-Xuan
 * @since 2019/7/9
 */
@Data
@ConfigurationProperties(prefix = "anti.reptile.manager")
public class AntiReptileProperties {

    /**
     * 是否启用反爬虫插件
     */
    private boolean enabled;

    /**
     * 是否启用全局拦截，默认为false，可设置为true全局拦截
     */
    private boolean globalFilterMode = false;

    /**
     * 非全局拦截下，需要反爬的接口列表，以'/'开头，以','分隔
     */
    private List<String> includeUrls;

    /**
     * 基于请求IP的反爬规则
     */
    private IpRule ipRule = new IpRule();

    /**
     * 基于请求User-Agent的反爬规则
     */
    private UaRule uaRule = new UaRule();


    @Data
    public static class IpRule {

        /**
         * 是否启用IP Rule：默认启用
         */
        private boolean enabled = true;

        /**
         * 时间窗口：默认5000ms
         */
        private Integer expirationTime = 5000;

        /**
         * 最大请求数，默认20
         */
        private Integer requestMaxSize = 20;

        /**
         * 命中规则后，锁定期限,默认10天，单位：秒（s）
         */
        private long lockExpire = TimeUnit.DAYS.toSeconds(1);
        /**
         * IP白名单，支持后缀'*'通配，以','分隔
         */
        private List<String> ignoreIp;

    }

    @Data
    public static class UaRule {
        /**
         * 是否启用User-Agent Rule：默认启用
         */
        private boolean enabled = true;

        /**
         * 是否允许Linux系统访问：默认否
         */
        private boolean allowedLinux = false;

        /**
         * 是否允许移动端设备访问：默认是
         */
        private boolean allowedMobile = true;

        /**
         *  是否允许移PC设备访问: 默认是
         */
        private boolean allowedPc = true;

        /**
         * 是否允许Iot设备访问：默认否
         */
        private boolean allowedIot = false;

        /**
         * 是否允许代理访问：默认否
         */
        private boolean allowedProxy = false;

    }
}
