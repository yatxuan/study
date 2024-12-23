package com.yat.test;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;

/**
 * <p>Description: 数据库密码测试 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/24 22:42
 */
@RequiredArgsConstructor
public class PasswordTest extends SpringBootDemoEmailApplicationTests {

    private final StringEncryptor encryptor;

    /**
     * 生成加密密码
     */
    @Test
    public void testGeneratePassword() {
        // 你的邮箱密码
        String password = "wjpmztwwfqmtbfie";
        // 加密后的密码(注意：配置上去的时候需要加 ENC(加密密码))
        String encryptPassword = encryptor.encrypt(password);
        String decryptPassword = encryptor.decrypt(encryptPassword);

        System.out.println("password = " + password);
        System.out.println("encryptPassword = " + encryptPassword);
        System.out.println("decryptPassword = " + decryptPassword);
    }
}
