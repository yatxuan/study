package com.yat.limit.controller;

import com.yat.limit.annotation.RateLimiter;
import com.yat.limit.config.AddressUtils;
import com.yat.limit.service.IRosterService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 10:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final IRosterService rosterService;
    private final RedisTemplate redisTemplate;

    @GetMapping("/image")
    public String image() {
        return "image";
    }

    @GetMapping("/sms")
    public String sms() {
        return "sms";
    }

    @GetMapping("/email")
    public String email() {
        return "email";
    }

    @GetMapping("/shiro")
    public String shiro() {
        return "shiro";
    }

    @GetMapping("/limitUrl")
    public String limitUrl() {
        return "limitUrl";
    }


    @GetMapping("/set")
    public void setList(HttpServletRequest request) {

        String ip = AddressUtils.getIpAddr(request);
        // 获取浏览器信息
        String browser = AddressUtils.getBrowser(request);
        // 获取详细地址
        String cityInfo = AddressUtils.getCityInfo(ip);
        // 把当前ip设置为黑名单用户
        // rosterService.setBlackList(ip);
        log.warn("当前ip'{}'为黑名单ip，禁止访问,浏览器为：'{}',消息地址为：'{}'", ip, browser, cityInfo);
    }

    @GetMapping("/limit/num")
    @RateLimiter
    public String limitNum() {
        return "LimitNum";
    }


    @Data
    @NoArgsConstructor
    class User {
        private String username;
        private String password;
        private int age;
    }
}
