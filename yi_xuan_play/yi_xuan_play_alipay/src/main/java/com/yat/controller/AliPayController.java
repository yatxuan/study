package com.yat.controller;

import com.yat.config.alipay.IAliPayApiService;
import com.yat.entity.vo.TradeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 支付宝PC网页支付
     * 买家测试账号：mgqxpv9809@sandbox.com
     * 买家测试密码：111111
     * 买家测试交易密码：111111
     *
     * @param trade 支付数据
     * @return 、
     * @throws Exception 、
     */
    @PostMapping("/toPayAsPC")
    public ResponseEntity<String> toPayAsPc(@Validated @RequestBody TradeVo trade) throws Exception {
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
    public ResponseEntity<String> toPayAsWeb(@Validated @RequestBody TradeVo trade) throws Exception {
        return aliPayApiService.toPayAsWeb(trade);
    }

    /**
     * 支付异步通知(要公网访问)，接收异步通知，
     *
     * @param request 、
     * @return 、
     */
    @RequestMapping("/notify")
    public ResponseEntity<Object> notify(HttpServletRequest request) {
        log.info("------------------------------支付异步通知------------------------------");
        return aliPayApiService.notify(request);
    }

    /**
     * 支付之后回调地址
     *
     * @param request  、
     * @param response 、
     * @return 、
     */
    @RequestMapping("/return")
    public ResponseEntity<String> returnPage(HttpServletRequest request, HttpServletResponse response) {
        log.info("------------------------------支付回调地址------------------------------");
        return aliPayApiService.returnPage(request, response);
    }

}
