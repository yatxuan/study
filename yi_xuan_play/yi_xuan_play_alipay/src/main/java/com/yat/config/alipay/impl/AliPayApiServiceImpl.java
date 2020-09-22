package com.yat.config.alipay.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.google.gson.Gson;
import com.yat.common.utils.AlipayUtils;
import com.yat.config.alipay.AliPayConfig;
import com.yat.config.alipay.IAliPayApiService;
import com.yat.entity.vo.TradeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.yat.common.utils.AliPayStatusEnum.FINISHED;
import static com.yat.common.utils.AliPayStatusEnum.SUCCESS;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/22 - 10:37
 */
@Slf4j
@Service("aliPayApiService")
@RequiredArgsConstructor
public class AliPayApiServiceImpl implements IAliPayApiService {

    private final AliPayConfig aliPayBean;
    private final AlipayClient alipayClient;


    @Override
    public ResponseEntity<String> toPayAsPc(TradeVo trade) throws Exception {

        trade.setOutTradeNo(AlipayUtils.getOrderCode());

        // 创建API对应的request(电脑网页版)
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 订单完成后返回的页面和异步通知地址
        request.setReturnUrl(aliPayBean.getReturnUrl());
        request.setNotifyUrl(aliPayBean.getNotifyUrl());
        // 填充订单参数
        request.setBizContent(getBizContent(trade));
        // 调用SDK生成表单, 通过GET方式，口可以获取url
        String payUrl = alipayClient.pageExecute(request, "GET").getBody();
        return ResponseEntity.ok(payUrl);
    }

    @Override
    public ResponseEntity<String> toPayAsWeb(TradeVo trade) throws Exception {

        trade.setOutTradeNo(AlipayUtils.getOrderCode());
        // 创建API对应的request(手机网页版)
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setReturnUrl(aliPayBean.getReturnUrl());
        request.setNotifyUrl(aliPayBean.getNotifyUrl());
        request.setBizContent(getBizContent(trade));
        String payUrl = alipayClient.pageExecute(request, "GET").getBody();
        return ResponseEntity.ok(payUrl);
    }



    @Override
    public ResponseEntity<String> returnPage(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=" + aliPayBean.getCharset());
        //内容验签，防止黑客篡改参数
        if (AlipayUtils.rsaCheck(request, aliPayBean)) {
            //商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(ISO_8859_1), UTF_8);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(ISO_8859_1), UTF_8);
            System.out.println("商户订单号" + outTradeNo + "  " + "第三方交易号" + tradeNo);

            // 根据业务需要返回数据，这里统一返回OK
            return new ResponseEntity<>("payment successful", OK);
        } else {
            // 根据业务需要返回数据
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Object> notify(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 内容验签，防止黑客篡改参数
        if (AlipayUtils.rsaCheck(request, aliPayBean)) {
            // 交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(ISO_8859_1), UTF_8);
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(ISO_8859_1), UTF_8);
            // 支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(ISO_8859_1), UTF_8);
            // 付款金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes(ISO_8859_1), UTF_8);
            // 验证
            if (tradeStatus.equals(SUCCESS.getValue()) || tradeStatus.equals(FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
                log.info("-------------验证通过处理订单---------------------");
            }
            return new ResponseEntity<>(OK);
        }
        return new ResponseEntity<>(BAD_REQUEST);
    }

    /**
     * 封装订单参数
     * @param trade 交易详情
     * @return 、
     */
    private String getBizContent(TradeVo trade) {
        Map<String,Object> extendParams = new HashMap<>(1);
        extendParams.put("sys_service_provider_id",aliPayBean.getSysServiceProviderId());

        Map<String,Object> bizContent = new HashMap<>(7);
        bizContent.put("out_trade_no",trade.getOutTradeNo());
        bizContent.put("product_code","FAST_INSTANT_TRADE_PAY");
        bizContent.put("total_amount",trade.getTotalAmount());
        bizContent.put("subject",trade.getSubject());
        bizContent.put("body",trade.getBody());
        bizContent.put("extend_params",extendParams);
        return new Gson().toJson(bizContent);
    }
}
