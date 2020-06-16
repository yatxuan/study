package com.yat.config.properties;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.yat.common.utils.StringUtils;
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
    @Value("${rsa.private_key}")
    private String publicKey;

    /**
     * 登录图形验证码有效时间/秒
     */
    @Value("${loginCode.expiration}")
    private Long expiration;

    @Value("${single.login:false}")
    private Boolean singleLogin;

    /**
     * RSA解密
     *
     * @param str 、
     * @return 、
     */
    public String getDecrypt(String str) {

        if (StringUtils.isBlank(str)) {
            return str;
        }

        RSA rsa = new RSA(privateKey, null);
        return new String(rsa.decrypt(str, KeyType.PrivateKey));
    }
     /**
     * RSA加密
     *
     * @param str 、
     * @return 、
     */
    public String getEncrypt(String str) {

        if (StringUtils.isBlank(str)) {
            return str;
        }

        RSA rsa = new RSA(null, publicKey);
        return new String(rsa.encrypt(str, KeyType.PublicKey));
    }



}
