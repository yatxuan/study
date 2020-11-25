package com.yat.config.service.impl;

import com.yat.common.annotation.RequestTag;
import com.yat.config.constant.StrategyType;
import com.yat.config.service.BaseStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 银联支付 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/25 - 14:55
 */
@Component
@RequiredArgsConstructor
@RequestTag(value = StrategyType.UNION_BY_PAY)
public class UnionStrategyServiceImpl implements BaseStrategy {

    @Override
    public void pay() {
        System.out.println("---------------银联支付---------------");
    }
}
