package com.yat.config.alipay.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.google.gson.Gson;
import com.yat.common.utils.AlipayUtils;
import com.yat.common.utils.ResultResponse;
import com.yat.config.alipay.AliPayConfig;
import com.yat.config.alipay.IAliPayApiService;
import com.yat.entity.vo.TradeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yat.common.utils.AliPayStatusEnum.FINISHED;
import static com.yat.common.utils.AliPayStatusEnum.SUCCESS;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

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
    public ResultResponse toPayAsPc(TradeVo trade) throws AlipayApiException {

        trade.setOutTradeNo(AlipayUtils.getOrderCode());

        // 创建API对应的request(电脑网页版)
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 订单完成后返回的页面和异步通知地址
        request.setReturnUrl(aliPayBean.getReturnUrl());
        request.setNotifyUrl(aliPayBean.getNotifyUrl());
        // 填充订单参数
        request.setBizContent(getBizContent(trade));
        // 调用SDK生成表单, 通过GET方式，口可以获取url
        String payUrl = alipayClient.pageExecute(request).getBody();
        return ResultResponse.success("操作成功", payUrl);
    }

    @Override
    public ResultResponse toPayAsWeb(TradeVo trade) throws Exception {

        // 商户订单号
        trade.setOutTradeNo(AlipayUtils.getOrderCode());
        // 创建API对应的request(手机网页版)
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setReturnUrl(aliPayBean.getReturnUrl());
        request.setNotifyUrl(aliPayBean.getNotifyUrl());
        request.setBizContent(getBizContent(trade));
        String payUrl = alipayClient.pageExecute(request, "GET").getBody();
        return ResultResponse.success("操作成功", payUrl);
    }


    @Override
    public ResultResponse returnPage(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=" + aliPayBean.getCharset());
        //内容验签，防止黑客篡改参数
        if (AlipayUtils.rsaCheck(request, aliPayBean)) {
            //商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(ISO_8859_1), UTF_8);
            //支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(ISO_8859_1), UTF_8);
            System.out.println("商户订单号" + outTradeNo + "  " + "第三方交易号" + tradeNo);

            // 根据业务需要返回数据，这里统一返回OK
            return ResultResponse.success("支付成功");
        } else {
            // 根据业务需要返回数据
            return ResultResponse.error("验证失败");
        }
    }

    @Override
    public void notify(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 内容验签，防止黑客篡改参数
        if (AlipayUtils.rsaCheck(request, aliPayBean)) {
            Map<String, String> map = getMap(request);
            // 交易状态
            String tradeStatus = map.get("tradeStatus");

            // 验证
            if (tradeStatus.equals(SUCCESS.getValue()) || tradeStatus.equals(FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
                log.info("-------------验证通过处理订单---------------------");
            }
            log.info(new Gson().toJson(map));
        }
        log.error("验证失败");
    }

    @Override
    public AlipayTradeQueryResponse checkingOrder(String outTradeNo, String tradeNo) throws AlipayApiException {

        Map<String, String> map = new HashMap<>(2);
        map.put("out_trade_no", outTradeNo);
        map.put("trade_no", tradeNo);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(new Gson().toJson(map));
        return alipayClient.execute(request);

    }

    @Override
    public ResultResponse refund(String outTradeNo, String tradeNo) throws AlipayApiException {

        AlipayTradeQueryResponse resultResponse = checkingOrder(outTradeNo, tradeNo);

        if (!"10000".equals(resultResponse.getCode())) {
            return ResultResponse.error("查询失败，订单不存在", resultResponse);
        }

        Map<String, Object> map = new HashMap<>(8);
        // 商户订单号
        map.put("out_trade_no", outTradeNo);
        // 支付宝交易号
        map.put("trade_no", tradeNo);
        // 退款金额
        String buyerPayAmount = "66";
        BigDecimal amount = new BigDecimal(buyerPayAmount);
        map.put("refund_amount", amount);
        // 退款备注
        map.put("refund_reason", "正常退款");
        // 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        map.put("out_request_no", StrUtil.swapCase(ObjectId.next()));
        // 操作员编号
        map.put("operator_id", "OP001");
        // 商店编号
        map.put("store_id", resultResponse.getStoreId());
        // 终端ID
        map.put("terminal_id", resultResponse.getTerminalId());

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(new Gson().toJson(map));
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return ResultResponse.success("退款成功", response);
        } else {
            return ResultResponse.error("退款失败", response);
        }
    }

    @Override
    public ResultResponse refundInquiry(String outTradeNo, String tradeNo, String outRequestNo) throws AlipayApiException {

        Map<String, String> map = new HashMap<>(8);
        // 商户订单号
        map.put("out_trade_no", outTradeNo);
        // 支付宝交易号
        map.put("trade_no", tradeNo);
        // 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
        map.put("out_request_no", outRequestNo);

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        request.setBizContent(new Gson().toJson(map));
        AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return ResultResponse.success("退款查询成功", response);
        } else {
            return ResultResponse.error("退款查询失败", response);
        }
    }

    @Override
    public ResultResponse closeTransaction(String outTradeNo, String tradeNo) throws AlipayApiException {

        Map<String, String> map = new HashMap<>(3);

        if (StrUtil.isBlank(tradeNo)) {
            if (StrUtil.isBlank(outTradeNo)) {
                return ResultResponse.error("关闭交易失败", "参数错误");
            }
            // 商户订单号
            map.put("out_trade_no", outTradeNo);
        } else {
            // 支付宝交易号
            map.put("trade_no", tradeNo);
        }

        // 卖家端自定义的的操作员 ID
        map.put("operator_id", "YX01");

        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizContent(new Gson().toJson(map));
        AlipayTradeCloseResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return ResultResponse.success("关闭交易成功", response);
        } else {
            return ResultResponse.error("关闭交易失败", response);
        }
    }

    @Override
    public ResultResponse getTheBill(String billDate) throws AlipayApiException {

        Map<String, String> map = new HashMap<>(8);
        // 账单类型：trade、signcustomer；
        // trade指商户基于支付宝交易收单的业务账单；
        // signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单。
        map.put("bill_type", "trade");
        // 账单时间：日账单格式为yyyy-MM-dd，
        map.put("bill_date", billDate);

        // 创建API对应的request类
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        // 设置业务参数
        request.setBizContent(new Gson().toJson(map));
        AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
        System.out.print(response.getBody());
        // 根据response中的结果继续业务逻辑处理
        if (response.isSuccess()) {
            downloadTheBill(response.getBillDownloadUrl());
            return ResultResponse.success("账单获取成功", response.getBillDownloadUrl());
        } else {
            return ResultResponse.error("账单获取失败");
        }
    }

    /**
     * 封装订单参数
     *
     * @param trade 交易详情
     * @return 、
     */
    private String getBizContent(TradeVo trade) {

        Map<String, Object> extendParams = new HashMap<>(1);
        extendParams.put("sys_service_provider_id", aliPayBean.getSysServiceProviderId());

        Map<String, Object> bizContent = new HashMap<>(7);
        bizContent.put("out_trade_no", trade.getOutTradeNo());
        //  销售产品码，与支付宝签约的产品码名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        // 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。类型：Price
        bizContent.put("total_amount", trade.getTotalAmount());
        bizContent.put("subject", trade.getSubject());
        bizContent.put("body", trade.getBody());
        // 绝对超时时间，格式为yyyy-MM-dd HH:mm:ss 一分钟后超时
        // bizContent.put("time_expire", DateUtil.offsetMinute(new Date(), 1).toString());
        bizContent.put("extend_params", extendParams);
        return new Gson().toJson(bizContent);
    }

    private Map<String, String> getMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(16);

        // 支付宝交易号
        String tradeNo = new String(request.getParameter("trade_no").getBytes(ISO_8859_1), UTF_8);
        // 开发者的app_id:支付宝分配给开发者的应用 ID
        String appId = new String(request.getParameter("app_id").getBytes(ISO_8859_1), UTF_8);
        // 商户订单号
        String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(ISO_8859_1), UTF_8);
        // 商户业务 ID，主要是退款通知中返回退款申请的流水号
        String outBizNo = new String(request.getParameter("out_biz_no").getBytes(ISO_8859_1), UTF_8);
        // 买家支付宝账号对应的支付宝唯一用户号。以 2088 开头的纯 16 位数字
        String buyerId = new String(request.getParameter("buyer_id").getBytes(ISO_8859_1), UTF_8);
        // 卖家支付宝用户号
        String sellerId = new String(request.getParameter("seller_id").getBytes(ISO_8859_1), UTF_8);
        // 交易状态
        String tradeStatus = new String(request.getParameter("trade_status").getBytes(ISO_8859_1), UTF_8);
        // 付款金额：本次交易支付的订单金额，单位为人民币（元），精确到小数点后2位
        String totalAmount = new String(request.getParameter("total_amount").getBytes(ISO_8859_1), UTF_8);
        // 实收金额：商家在交易中实际收到的款项，单位为元，精确到小数点后2位
        String receiptAmount = new String(request.getParameter("receipt_amount").getBytes(ISO_8859_1), UTF_8);
        // 开票金额：用户在交易中支付的可开发票的金额，单位为元，精确到小数点后2位
        String invoiceAmount = new String(request.getParameter("invoice_amount").getBytes(ISO_8859_1), UTF_8);
        // 付款金额:用户在交易中支付的金额，单位为元，精确到小数点后2位
        String buyerPayAmount = new String(request.getParameter("buyer_pay_amount").getBytes(ISO_8859_1), UTF_8);
        // 集分宝金额:使用集分宝支付的金额，单位为元，精确到小数点后2位
        String pointAmount = new String(request.getParameter("point_amount").getBytes(ISO_8859_1), UTF_8);
        // 总退款金额:退款通知中，返回总退款金额，单位为元，精确到小数点后2位
        String refundFee = new String(request.getParameter("refund_fee").getBytes(ISO_8859_1), UTF_8);
        // 订单标题:商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来
        String subject = new String(request.getParameter("subject").getBytes(ISO_8859_1), UTF_8);
        // 商品描述:该订单的备注、描述、明细等。对应请求时的body参数，原样通知回来
        String body = new String(request.getParameter("body").getBytes(ISO_8859_1), UTF_8);
        // 交易创建时间:该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss
        String gmtCreate = new String(request.getParameter("gmt_create").getBytes(ISO_8859_1), UTF_8);
        // 交易付款时间:该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss
        String gmtPayment = new String(request.getParameter("gmt_payment").getBytes(ISO_8859_1), UTF_8);
        // 交易退款时间:该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.S
        String gmtRefund = new String(request.getParameter("gmt_refund").getBytes(ISO_8859_1), UTF_8);
        // 交易结束时间	:该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss
        String gmtClose = new String(request.getParameter("gmt_close").getBytes(ISO_8859_1), UTF_8);
        // 支付金额信息	:支付成功的各个渠道金额信息
        String fundBillList = new String(request.getParameter("fund_bill_list").getBytes(ISO_8859_1), UTF_8);
        // 优惠券信息	:本交易支付时所使用的所有优惠券信息
        String voucherDetailList = new String(request.getParameter("voucher_detail_list").getBytes(ISO_8859_1), UTF_8);
        // 回传参数:公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝
        String passbackParams = new String(request.getParameter("passback_params").getBytes(ISO_8859_1), UTF_8);

        map.put("tradeNo", tradeNo);
        map.put("appId", appId);
        map.put("outTradeNo", outTradeNo);
        map.put("outBizNo", outBizNo);
        map.put("buyerId", buyerId);
        map.put("sellerId", sellerId);
        map.put("tradeStatus", tradeStatus);
        map.put("totalAmount", totalAmount);
        map.put("receiptAmount", receiptAmount);
        map.put("invoiceAmount", invoiceAmount);
        map.put("buyerPayAmount", buyerPayAmount);
        map.put("pointAmount", pointAmount);
        map.put("refundFee", refundFee);
        map.put("subject", subject);
        map.put("body", body);
        map.put("gmtCreate", gmtCreate);
        map.put("gmtPayment", gmtPayment);
        map.put("gmtRefund", gmtRefund);
        map.put("gmtClose", gmtClose);
        map.put("fundBillList", fundBillList);
        map.put("voucherDetailList", voucherDetailList);
        map.put("passbackParams", passbackParams);

        return map;
    }

    /**
     * 下载账单
     */
    private void downloadTheBill(String urlStr) {
        // 带进度显示的文件下载
        HttpUtil.downloadFile(urlStr, FileUtil.file("d:/"), new StreamProgress() {
            @Override
            public void start() {
                Console.log("开始下载。。。。");
            }

            @Override
            public void progress(long progressSize) {
                Console.log("已下载：{}", FileUtil.readableFileSize(progressSize));
            }

            @Override
            public void finish() {
                Console.log("下载完成！");
            }
        });

    }
}
