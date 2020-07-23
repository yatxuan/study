package com.yat.encryption;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.security.KeyPair;

/**
 * <p>Description: 非对称加密 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/9 - 17:28
 */
public class AsymmetricCrypto {
    public static void main(String[] args) {
        // AES aes = SecureUtil.aes();
        rsaEncryption();
    }

    /**
     * RSA 加密
     */
    private static void rsaEncryption() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");

        // String publicKey = Base64.encode(pair.getPublic().getEncoded());
        // String privateKey = Base64.encode(pair.getPrivate().getEncoded());

        String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOC5FjXCknqDK9HbZHRcmc8rhxrZ3zIj6ZUF8AmXX2jXuF+qZKjC+/V0zCtm0IRhxGKEYim6tqOi3HoDT6a4PFB4ia0osoBuu0WYFwVdrWEHS/Vpolwmv7WJdyhSCXv6ontEUjS4rgNX67EVe+omdeFxZeQshqDMd9WMm6eFJQ03AgMBAAECgYBRxujvLVwymxNSLf1zXtNWEaf7vHc9ftYctelxBBXjSEU/AvnYdAROgW9fB+4vjohxtW0ndFvMVpLXD+dnazn+/AD5sdQAfgXWS2xHXEjJXJZ2ANVNU79a0bM2JyDZB8xJaHLluIt38yOx9RwkdBy7FKSCXKc/4bNZjmoBxOTCmQJBAPh/Xd7JhwqF/ANuLZcpy3W2mXeAPRD7dFR+Jv9jUHB8IbV1IYbXlzwTpMpRZdWI7k2UbwlL+3EmGYcLL/OeYNMCQQDngfeU5wchhhSSieuCdSPK+Gs06xBg7lDjletYpkaVOV5erKlyglYC4UdE+KwCKrDaKhx+KaXfW7QqtZg+k8ONAkEAgZXp3cQ6J8K3KRLLs4iYEUqnvSmirrhycZ1XuNaRnBcffQEX+rlypGGaDedks3Y0z0AYp0B4Xwj/Ru0OlHyDAQJBAJ/Q7tNSMXAJGYaXw4cJyq3BlHCxnVrs6dZSlmdaUfuN4QHGfCtaonnWfta0PDo6sl/JFmZZ5fItYlDhj7oTU7kCQQCrYe1VBxJPfEl12FazVwot7Dlx8ZGgnw6XfXrquH5BZdRn0AfyNrB9IlYW4R891OZiQi7N2xzQhneolW1D2Iel";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDguRY1wpJ6gyvR22R0XJnPK4ca2d8yI+mVBfAJl19o17hfqmSowvv1dMwrZtCEYcRihGIpurajotx6A0+muDxQeImtKLKAbrtFmBcFXa1hB0v1aaJcJr+1iXcoUgl7+qJ7RFI0uK4DV+uxFXvqJnXhcWXkLIagzHfVjJunhSUNNwIDAQAB";


        String json = "admin";

        RSA rsa = new RSA(privateKey, publicKey);

        // 加密
        byte[] encrypt = rsa.encrypt(json, KeyType.PublicKey);
        String encode = Base64.encode(encrypt);

        // 解密
        byte[] decrypt = rsa.decrypt(encode, KeyType.PrivateKey);

        System.out.println(encode);
        System.out.println(new String(decrypt));
    }
}
