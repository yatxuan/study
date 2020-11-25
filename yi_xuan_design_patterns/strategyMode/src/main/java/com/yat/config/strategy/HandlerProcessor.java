package com.yat.config.strategy;

import com.yat.common.annotation.RequestTag;
import com.yat.config.service.BaseStrategy;
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
public class HandlerProcessor implements ApplicationContextAware {

    /**
     * 1、程序启动时，先通过‘HandlerProcessor’类，把定义的所有对象存储到Map里
     * 2、在代码调用时，通过‘HandlerContext’ 获取对应的策略类运行
     * 获取所有的策略BeanClass 加入HandlerOrderContext属性中
     *
     * @param applicationContext 、
     * @throws BeansException 、
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取所有策略注解的Bean
        Map<String, Object> orderStrategyMap = applicationContext.getBeansWithAnnotation(RequestTag.class);
        if (!orderStrategyMap.isEmpty()) {
            orderStrategyMap.forEach((k, v) -> {
                if (v instanceof BaseStrategy) {
                    // 获取策略实现类
                    Class<BaseStrategy> orderStrategyClass = (Class<BaseStrategy>) v.getClass();
                    // 获取策略实现类的注解值。
                    String type = orderStrategyClass.getAnnotation(RequestTag.class).value();
                    // 将class加入HandlerOrderContext的map中,type作为key
                    HandlerContext.STRATEGY_BEAN_MAP.put(type, orderStrategyClass);
                }
            });
        }
    }
}
