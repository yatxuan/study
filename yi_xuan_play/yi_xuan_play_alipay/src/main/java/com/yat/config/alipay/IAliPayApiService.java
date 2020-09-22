package com.yat.config.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.yat.common.utils.ResultResponse;
import com.yat.entity.vo.TradeVo;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/22 15:37
 */
public interface IAliPayApiService {

    /**
     * 支付宝PC网页支付
     *
     * @param trade 支付数据
     * @return 、
     * @throws AlipayApiException 、
     */
    ResultResponse toPayAsPc(TradeVo trade) throws AlipayApiException;

    /**
     * 支付宝手机网页支付
     *
     * @param trade 支付数据
     * @return 、
     * @throws Exception 、
     */
    ResultResponse toPayAsWeb(TradeVo trade) throws Exception;

    /**
     * 支付之后跳转的链接
     *
     * @param request  、
     * @param response 、
     * @return 、
     */
    ResultResponse returnPage(HttpServletRequest request, HttpServletResponse response);

    /**
     * 支付异步通知(要公网访问)，接收异步通知，
     * 检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，
     * 根据trade_status进行后续业务处理
     *
     * @param request 、
     * @return 、
     */
    void notify(HttpServletRequest request);

    /**
     * 查询订单
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号
     * @return 、
     * @throws AlipayApiException 、
     */
    AlipayTradeQueryResponse checkingOrder(String outTradeNo, String tradeNo) throws AlipayApiException;

    /**
     * 退款
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号
     * @return 、
     * @throws AlipayApiException 、
     */
    ResultResponse refund(String outTradeNo, String tradeNo) throws AlipayApiException;

    /**
     * 退款
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号
     * @return 、
     * @throws AlipayApiException 、
     */
    ResultResponse refundInquiry(String outTradeNo, String tradeNo) throws AlipayApiException;

    /**
     * 关闭交易
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号
     * @return 、
     * @throws AlipayApiException 、
     */
    ResultResponse closeTransaction(String outTradeNo, String tradeNo) throws AlipayApiException;

    /**
     * 下载账单
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号
     * @return 、
     * @throws AlipayApiException 、
     */
    ResultResponse downloadTheBill(String outTradeNo, String tradeNo) throws AlipayApiException;
}
