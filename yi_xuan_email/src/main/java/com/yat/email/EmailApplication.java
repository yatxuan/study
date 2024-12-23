package com.yat.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p>Description: 描述:@EnableAsync-启用异步执行 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/8
 * @Time: 10:45
 */
@EnableAsync
@SpringBootApplication
public class EmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class);
    }
}
