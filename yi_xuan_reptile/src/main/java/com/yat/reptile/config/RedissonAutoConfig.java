package com.yat.reptile.config;


import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 没有redisson starter时才加载
 * @author Yat-Xuan
 * @since 2019/7/17 8:21
 */
@Data
@Configuration
@ConditionalOnMissingClass("org.redisson.spring.starter.RedissonAutoConfiguration")
public class RedissonAutoConfig {


    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redisson() throws IOException {
        // 本例子使用的是yaml格式的配置文件，读取使用Config.fromYAML，如果是Json文件，则使用Config.fromJSON
        Config config = Config.fromYAML(RedissonAutoConfig.class.getClassLoader().getResource("redisson.yml"));
        return Redisson.create(config);
    }

}
