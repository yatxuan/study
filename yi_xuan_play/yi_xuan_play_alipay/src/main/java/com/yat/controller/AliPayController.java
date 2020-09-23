package com.yat.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.yat.common.utils.ResultResponse;
import com.yat.config.alipay.IAliPayApiService;
import com.yat.entity.vo.TradeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Description: 支付宝支付 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/22 - 11:40
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aliPay")
public class AliPayController {

    private final IAliPayApiService aliPayApiService;

    /**
     * PC端支付测试
     *
     * @param response 、
     * @throws Exception 、
     */
    @GetMapping
    public void test(HttpServletResponse response) throws Exception {
        TradeVo trade = new TradeVo();
        trade.setSubject("iPhone12");
        trade.setTotalAmount("300");
        trade.setBody("全新的iPhone手机");

        ResultResponse resultResponse = aliPayApiService.toPayAsPc(trade);
        String body = String.valueOf(resultResponse.get("data"));
        response.setContentType("text/html;charset=utf-8");
        //直接将完整的表单html输出到页面
        assert body != null;
        response.getWriter().write(body);
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 支付宝PC网页支付
     * 买家测试账号：mgqxpv9809@sandbox.com
     * 买家测试密码：111111
     * 买家测试交易密码：111111
     *
     * @param trade 支付数据
     * @return 、
     * @throws AlipayApiException 、
     */
    @PostMapping("/toPayAsPC")
    public ResultResponse toPayAsPc(@Validated @RequestBody TradeVo trade) throws AlipayApiException {
        return aliPayApiService.toPayAsPc(trade);
    }

    /**
     * 支付宝手机网页支付
     *
     * @param trade 支付数据
     * @return 、
     * @throws Exception 、
     */
    @PostMapping(value = "/toPayAsWeb")
    public ResultResponse toPayAsWeb(@Validated @RequestBody TradeVo trade) throws Exception {
        return aliPayApiService.toPayAsWeb(trade);
    }

    /**
     * 支付异步通知(要公网访问)，接收异步通知，
     *
     * @param request 、
     * @return 、
     */
    @RequestMapping("/notify")
    public ResultResponse notify(HttpServletRequest request) {
        log.info("------------------------------支付异步通知------------------------------");
        aliPayApiService.notify(request);
        return ResultResponse.success();
    }

    /**
     * 支付之后-回调地址
     *
     * @param request  、
     * @param response 、
     * @return 、
     */
    @RequestMapping("/return")
    public ResultResponse returnPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("------------------------------支付回调地址------------------------------");
        return aliPayApiService.returnPage(request, response);
    }

    /**
     * 订单查询接口
     *
     * @param outTradeNo 商户订单号
     * @return 、
     */
    @RequestMapping("/checking/order")
    public ResultResponse checkingOrder(String outTradeNo) throws AlipayApiException {
        log.info("------------------------------支付订单查询------------------------------");
        AlipayTradeQueryResponse response = aliPayApiService.checkingOrder(outTradeNo, null);

        if (response.isSuccess()) {
            return ResultResponse.success(response);
        } else {
            return ResultResponse.error("查询失败", response);
        }
    }

    /**
     * 退款
     * 商户订单号202009231043015824  第三方交易号2020092322001498000508914248
     *
     * @return 、
     */
    @RequestMapping("/refund")
    public ResultResponse refund() throws AlipayApiException {
        // 商户订单号
        String outTradeNo = "202009231043015824";
        // 支付宝交易号
        String tradeNo = "2020092322001498000508914248";
        log.info("------------------------------支付退款------------------------------");
        return aliPayApiService.refund(outTradeNo, tradeNo);
    }

    /**
     * 退款查询
     *
     * @return 、
     */
    @RequestMapping("/refundInquiry")
    public ResultResponse refundInquiry() throws AlipayApiException {
        // 商户订单号
        String outTradeNo = "202009221628058103";
        // 支付宝交易号
        String tradeNo = "2020092222001498000508804301";
        // 请求退款接口时，传入的退款请求号
        String outRequestNo = "HZ01RF001222";
        log.info("------------------------------支付退款查询------------------------------");
        return aliPayApiService.refundInquiry(outTradeNo, tradeNo, outRequestNo);
    }

    /**
     * 关闭交易
     *
     * @param outTradeNo 商户订单号
     * @return 、
     */
    @RequestMapping("/closeTransaction")
    public ResultResponse closeTransaction(String outTradeNo) throws AlipayApiException {
        log.info("------------------------------关闭交易------------------------------");
        return aliPayApiService.closeTransaction(outTradeNo, null);
    }

    /**
     * 下载账单-报错：入参错误，参考文档：https://opendocs.alipay.com/open/270/105899
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd
     * @return 、
     */
    @RequestMapping("/downloadTheBill")
    public ResultResponse downloadTheBill(String billDate) throws AlipayApiException {
        log.info("------------------------------下载账单------------------------------");
        return aliPayApiService.getTheBill(billDate);
    }


}
