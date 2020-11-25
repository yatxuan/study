package com.yat.config.strategy;

import com.yat.common.SpringUtils;
import com.yat.config.service.BaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/24 20:59
 */
@Component
public class HandlerContext {

    /**
     * 存放所有策略类Bean的map
     */
    static Map<String, Class<BaseStrategy>> STRATEGY_BEAN_MAP = new HashMap<>(16);

    public BaseStrategy getOrderStrategy(String type) {
        Class<BaseStrategy> strategyClass = STRATEGY_BEAN_MAP.get(type);
        if (null == strategyClass) {
            throw new IllegalArgumentException("没有对应的策略类型：" + type);
        }
        // 从容器中获取对应的策略Bean
        return SpringUtils.getBean(strategyClass);
    }
}
