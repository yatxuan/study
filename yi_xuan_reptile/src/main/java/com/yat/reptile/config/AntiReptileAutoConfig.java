package com.yat.reptile.config;

import com.yat.config.cross.WebMvcConfig;
import com.yat.reptile.rule.AntiReptileRule;
import com.yat.reptile.rule.RuleActuator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RedissonAutoConfiguration 的 AutoConfigureOrder 为默认值(0)，此处在它后面加载
 *
 * @author Yat-Xuan
 * @since 2019/7/8
 */
@Configuration
@EnableConfigurationProperties(AntiReptileProperties.class)
@ConditionalOnProperty(prefix = "anti.reptile.manager", value = "enabled", havingValue = "true")
@Import({RedissonAutoConfig.class, WebMvcConfig.class})
public class AntiReptileAutoConfig {

    @Bean
    public RuleActuator ruleActuator(final List<AntiReptileRule> rules) {
        final List<AntiReptileRule> antiReptileRules = rules.stream()
                .sorted(Comparator.comparingInt(AntiReptileRule::getOrder)).collect(Collectors.toList());
        return new RuleActuator(antiReptileRules);
    }


}
