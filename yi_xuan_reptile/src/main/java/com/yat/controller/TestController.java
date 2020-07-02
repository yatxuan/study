package com.yat.controller;

import cn.keking.anti_reptile.annotation.AntiReptile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/2 - 17:01
 */
@RequestMapping("/test")
@RestController
public class TestController {


    /**
     * 注解模式：
     * 在需要反爬的接口Controller对象对应的接口上加上@AntiReptile注解即可，示例如下
     */
    @GetMapping
    @AntiReptile
    public String demo() {
        return "Hello，World!";
    }
}
