package com.yat.limit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 10:28
 */
@SpringBootApplication
public class LimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimitApplication.class);
        System.out.println("------------------------------程序初始化完成---------------------------------");
    }
}
