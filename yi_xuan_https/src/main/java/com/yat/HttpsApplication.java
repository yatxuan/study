package com.yat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/29 - 14:54
 */
@RestController
@SpringBootApplication
public class HttpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpsApplication.class);
    }

    @GetMapping("/test")
    public String test(){
        return "Https----->success";
    }
}
