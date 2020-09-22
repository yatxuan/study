package com.yat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/22 - 15:20
 */
@SpringBootApplication
public class AlipayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlipayApplication.class);
        System.out.println("--------------------程序初始化完成---------------------------");
    }
}
