package com.yat.limit.controller;

import com.yat.limit.annotation.RateLimiter;
import com.yat.limit.config.AddressUtils;
import com.yat.limit.service.IRosterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    @RateLimiter
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
        rosterService.setBlackList(ip);
        log.warn("当前ip'{}'为黑名单ip，禁止访问,浏览器为：'{}',消息地址为：'{}'", ip, browser, cityInfo);

    }

}
