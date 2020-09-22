package com.yat.config.alipay;

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
     * @throws Exception 、
     */
    ResponseEntity<String> toPayAsPc(TradeVo trade) throws Exception;

    /**
     * 支付宝手机网页支付
     *
     * @param trade 支付数据
     * @return 、
     * @throws Exception 、
     */
    ResponseEntity<String> toPayAsWeb(TradeVo trade) throws Exception;

    /**
     * 支付之后跳转的链接
     *
     * @param request  、
     * @param response 、
     * @return 、
     */
    ResponseEntity<String> returnPage(HttpServletRequest request, HttpServletResponse response);

    /**
     * 支付异步通知(要公网访问)，接收异步通知，
     * 检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，
     * 根据trade_status进行后续业务处理
     *
     * @param request 、
     * @return 、
     */
    ResponseEntity<Object> notify(HttpServletRequest request);


}
