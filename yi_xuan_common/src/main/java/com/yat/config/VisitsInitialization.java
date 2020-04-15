package com.yat.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化站点统计
 *
 * @author yat
 */
@Component
public class VisitsInitialization implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) {
        System.out.println("--------------- 初始化系统完成 ---------------");
    }
}
