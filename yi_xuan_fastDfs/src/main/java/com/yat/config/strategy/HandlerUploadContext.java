package com.yat.config.strategy;

import com.yat.utils.spring.SpringUtils;
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
public class HandlerUploadContext {

    /**
     * 存放所有策略类Bean的map
     */
    static Map<Integer, Class<BaseUploadStrategy>> UPLOAD_STRATEGY_BEAN_MAP = new HashMap<>();

    public BaseUploadStrategy getOrderStrategy(Integer type) {
        Class<BaseUploadStrategy> strategyClass = UPLOAD_STRATEGY_BEAN_MAP.get(type);
        if (null == strategyClass) {
            throw new IllegalArgumentException("没有对应的文件类型上传方法：" + type);
        }
        // 从容器中获取对应的策略Bean
        return SpringUtils.getBean(strategyClass);
    }
}
