package com.yat.social.config.strategy.oauth;

import cn.hutool.core.util.StrUtil;
import com.yat.social.common.exception.CustomException;
import com.yat.social.common.utils.spring.SpringUtils;
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
public class HandlerOauthContext {

    /**
     * 存放所有策略类Bean的map
     */
    static Map<String, Class<BaseOauthStrategy>> OAUTH_STRATEGY_BEAN_MAP = new HashMap<>(16);

    /**
     * 授权
     *
     * @param oauthType 授权类型
     * @return 、
     */
    public BaseOauthStrategy getOrderStrategy(String oauthType) {

        if (StrUtil.isBlank(oauthType)) {
            throw new CustomException("不支持的类型");
        }

        Class<BaseOauthStrategy> strategyClass = OAUTH_STRATEGY_BEAN_MAP.get(oauthType);
        if (null == strategyClass) {
            throw new CustomException("错误的授权类型：" + oauthType);
        }
        // 从容器中获取对应的策略Bean
        return SpringUtils.getBean(strategyClass);
    }
}
