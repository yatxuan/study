package com.yat.split.file;

import com.yat.split.file.common.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>Description: 拆分Yml文件 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/2 - 17:12
 */
@RestController
@SpringBootApplication
public class SplitFileApplication {

    @Resource
    private Test test;

    public static void main(String[] args) {
        SpringApplication.run(SplitFileApplication.class);
        System.out.println("--------------------------------系统初始化结束-----------------------------------");
    }

    @GetMapping("/test")
    public String get() {
        return test.toString();
    }
}

