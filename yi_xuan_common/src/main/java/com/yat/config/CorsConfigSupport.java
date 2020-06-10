package com.yat.config;

import com.yat.config.properties.IgnoredUrlsProperties;
import com.yat.config.redis.interceptor.LimitRaterInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * <p>Description: 跨域处理:一 </p>
 * <p>因为spring新版本中不提倡实现：WebMvcConfigurerAdapter，推荐的方式为：</p>
 * <p>一：实现：WebMvcConfigurationSupport 父类，
 * （实现该父类，会导致SpringMVC原有的默认配置失效，如果对原理不是很清楚的开发者，建议使用下面的方式）</p>
 * <p>二：继承：WebMvcConfigurer 接口，加上@EnableWebMvc注解 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2019/9/10
 * @Time: 17:27
 */
@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class CorsConfigSupport implements WebMvcConfigurer {


    private final IgnoredUrlsProperties ignoredUrlsProperties;
    private final LimitRaterInterceptor limitRaterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 跨域+限流拦截器需放在最上面
        InterceptorRegistration ir = registry.addInterceptor(limitRaterInterceptor);
        // 配置拦截的路径
        ir.addPathPatterns("/**");
        // 配置不拦截的路径 避免加载css也拦截（可根据实际情况放开限流配置或拦截路径）
        ir.excludePathPatterns(ignoredUrlsProperties.getLimitUrls());
    }

    /**
     * 继承WebMvcConfigurationSupport后，
     * 会导致springMVC的静态化自动配置失效，
     * 所以要自己手动在这里配置映射路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("接口访问的路径")
        //         .addResourceLocations("项目里对应的静态文件的路径");

        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico");

    }
    //
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowedHeaders("*")
    //             .allowedOrigins("*")
    //             .allowCredentials(true)
    //             .allowedMethods("*")
    //             .maxAge(3600);
    // }
}
