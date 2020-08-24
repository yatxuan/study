package com.yat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/5
 * @Time: 13:41
 */
@SpringBootApplication
public class FastDfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastDfsApplication.class);
        System.out.println("------------------------程序初始化完成--------------------------");
    }
}
