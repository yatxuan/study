package com.yat.test;

import com.yat.social.SocialApplication;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Description: 数据库密码测试 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/24 22:42
 */
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
@SpringBootTest(classes = SocialApplication.class)
public class PasswordTest {

    private final StringEncryptor encryptor;

    /**
     * 生成加密密码
     */
    @Test
    public void testGeneratePassword() {
        // 你的密码
        String password = "3f8c378aa507dbe0a0840a7a32b6e864d1fcc140";
        // 加密后的密码(注意：配置上去的时候需要加 ENC(加密密码))
        String encryptPassword = encryptor.encrypt(password);
        String decryptPassword = encryptor.decrypt(encryptPassword);

        System.out.println("password = " + password);
        System.out.println("encryptPassword = " + encryptPassword);
        System.out.println("decryptPassword = " + decryptPassword);
    }
}
