package com.yat.config.constant;

/**
 * <p>Description: 定义策略类型 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/25 - 14:46
 */
public interface StrategyType {

    /**
     * 支付宝支付
     */
    String ALIPAY_BY_PAY = "ALiPay";
    /**
     * 微信支付
     */
    String WECHAT_BY_PAY = "WeChat";
    /**
     * QQ支付
     */
    String QQ_BY_PAY = "QQ";
    /**
     * 银联支付
     */
    String UNION_BY_PAY = "Union";
}
