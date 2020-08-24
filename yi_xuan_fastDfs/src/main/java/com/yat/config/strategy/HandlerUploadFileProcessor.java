package com.yat.config.strategy;

import com.yat.annotation.HandlerFileUploadType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>Description: 自定义spring处理器。把所有的策略类存储到Map集合中。 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/24 20:55
 */
@Component
public class HandlerUploadFileProcessor implements ApplicationContextAware {
    /**
     * 获取所有的策略BeanClass 加入HandlerOrderContext属性中
     *
     * @param applicationContext 、
     * @throws BeansException 、
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取所有策略注解的Bean
        Map<String, Object> orderStrategyMap = applicationContext.getBeansWithAnnotation(HandlerFileUploadType.class);
        if (orderStrategyMap.size() > 0) {
            orderStrategyMap.forEach((k, v) -> {
                if (v instanceof BaseUploadStrategy) {
                    // 获取策略实现类
                    Class<BaseUploadStrategy> orderStrategyClass = (Class<BaseUploadStrategy>) v.getClass();
                    // 获取策略实现类的注解值。
                    int type = orderStrategyClass.getAnnotation(HandlerFileUploadType.class).value();
                    // 将class加入HandlerOrderContext的map中,type作为key
                    HandlerUploadContext.UPLOAD_STRATEGY_BEAN_MAP.put(type, orderStrategyClass);
                }
            });
        }
    }
}
