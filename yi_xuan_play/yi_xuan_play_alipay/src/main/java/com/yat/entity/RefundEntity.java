package com.yat.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>Description: 交易退款 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/23 - 9:49
 */
@Data
@SuppressWarnings("all")
public class RefundEntity {

    /**
     * 订单支付时传入的商户订单号,不能和 trade_no同时为空。
     */
    private String out_trade_no;
    /**
     * 支付宝交易号，和商户订单号不能同时为空
     */
    private String trade_no;
    /**
     * 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     */
    private BigDecimal refund_amount;
    private String refund_currency;
    private String refund_reason;
    private String out_request_no;
    private String operator_id;
    private String store_id;
    private String terminal_id;
    private String org_pid;
    private List<GoodsDetailBean> goods_detail;
    private List<RefundRoyaltyParametersBean> refund_royalty_parameters;
    private List<String> query_options;

    @Data
    public static class GoodsDetailBean {

        /**
         * 商品的编号
         */
        private String goods_id;
        /**
         * 支付宝定义的统一商品编号
         */
        private String alipay_goods_id;
        /**
         * 商品名称
         */
        private String goods_name;
        /**
         * 商品数量
         */
        private Number quantity;
        /**
         * 商品单价，单位为元
         */
        private BigDecimal price;
        /**
         * 商品类目
         */
        private String goods_category;
        /**
         * 商品类目树，从商品类目根节点到叶子节点的类目id组成，类目id值使用|分割
         */
        private String categories_tree;
        /**
         * 商品描述信息
         */
        private String body;
        /**
         * 商品的展示地址
         */
        private String show_url;


    }

    @Data
    public static class RefundRoyaltyParametersBean {

        private String royalty_type;
        private String trans_out;
        private String trans_out_type;
        private String trans_in_type;
        private String trans_in;
        private double amount;
        private int amount_percentage;
        private String desc;

    }
}
