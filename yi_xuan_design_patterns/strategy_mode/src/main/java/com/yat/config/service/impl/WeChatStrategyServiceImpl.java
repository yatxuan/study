package com.yat.config.service.impl;

import com.yat.common.annotation.RequestTag;
import com.yat.config.constant.StrategyType;
import com.yat.config.service.BaseStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 微信支付 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/25 - 14:53
 */
@Component
@RequiredArgsConstructor
@RequestTag(value = StrategyType.WECHAT_BY_PAY)
public class WeChatStrategyServiceImpl implements BaseStrategy {

    @Override
    public void pay() {
        System.out.println("---------------微信支付---------------");
    }
}
