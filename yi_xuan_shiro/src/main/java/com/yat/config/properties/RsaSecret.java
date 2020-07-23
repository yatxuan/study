package com.yat.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description:  </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/23
 * @Time: 14:36
 */
@Data
@Configuration
public class RsaSecret {
    /**
     * 后端私钥
     */
    @Value("${rsa.private_key}")
    private String privateKey;
    /**
     * 后端公钥
     */
    @Value("${rsa.public_key}")
    private String publicKey;

    /**
     * 登录图形验证码有效时间/秒
     */
    @Value("${loginCode.expiration}")
    private Long expiration;
}
