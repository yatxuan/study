package com.yat.common.constant;

/**
 * <p>Description: 支付宝支付：交易状态 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/22 - 18:16
 */
public interface TradingConstant {
    /**
     * 交易创建，等待买家付款（不触发通知）
     */
    String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    /**
     * 未付款交易超时关闭，或支付完成后全额退款（不触发通知）
     */
    String TRADE_CLOSED = "TRADE_CLOSED";
    /**
     * 交易支付成功（触发通知）
     */
    String TRADE_SUCCESS = "TRADE_SUCCESS";
    /**
     * 交易结束，不可退款（不触发通知）
     */
    String TRADE_FINISHED = "TRADE_FINISHED";
}
