// package com.yat.social.justauth;
//
// import lombok.Getter;
// import lombok.Setter;
// import me.zhyd.oauth.config.AuthConfig;
// import me.zhyd.oauth.config.AuthDefaultSource;
// import me.zhyd.oauth.config.AuthSource;
// import org.springframework.boot.autoconfigure.cache.CacheProperties;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.context.properties.NestedConfigurationProperty;
// import org.springframework.context.annotation.Configuration;
//
// import java.util.HashMap;
// import java.util.Map;
//
// /**
//  * <p>
//  * JustAuth自动装配配置类
//  * </p>
//  *
//  * @author yangkai.shen
//  * @date Created in 2019-07-22 10:59
//  */
// @Getter
// @Setter
// @Configuration
// @ConfigurationProperties(prefix = "justauth")
// public class JustAuthProperties {
//     /**
//      * 是否启用 JustAuth
//      */
//     private boolean enabled;
//
//     /**
//      * JustAuth 配置
//      */
//     private Map<AuthDefaultSource, AuthConfig> type = new HashMap<>();
//
//     /**
//      * 缓存配置类
//      */
//     @NestedConfigurationProperty
//     private CacheProperties cache;
//
// }
