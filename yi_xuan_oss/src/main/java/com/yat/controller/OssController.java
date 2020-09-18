package com.yat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/18 - 10:14
 */
@RestController
@RequestMapping("/oss")
public class OssController {

    @GetMapping
    public String hello() {
        System.out.println("------------------9010进入程序-------------------");
        return "oss访问成功";
    }
}
