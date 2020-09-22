package com.yat.config.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>支付宝配置 Bean</p>
 *
 * @author Javen
 */
@Data
@Component
@PropertySource("classpath:/production/alipay.properties")
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {

    /**
     * 应用ID,AppID，收款账号既是AppID对应支付宝账号
     */
    private String appId;
    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    private String privateKey;
    /**
     * 支付宝公钥
     */
    private String publicKey;
    /**
     * 签名方式，固定格式
     */
    private String signType = "RSA2";
    /**
     * 支付宝开放安全地址，一般不会变
     */
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
    /**
     * 编码，固定格式
     */
    private String charset = "utf-8";
    /**
     * 异步通知地址
     */
    private String notifyUrl;
    /**
     * 订单完成后返回的页面
     */
    private String returnUrl;
    /**
     * 类型，固定格式
     */
    private String format = "JSON";
    /**
     * 商户号
     */
    private String sysServiceProviderId;


    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                gatewayUrl, appId, privateKey, format,
                charset, publicKey, signType
        );
    }
}
