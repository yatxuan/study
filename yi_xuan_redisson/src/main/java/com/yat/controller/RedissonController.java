package com.yat.controller;

import com.yat.config.redis.RedissonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/23 - 14:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/redisson")
public class RedissonController {

    private final RedissonUtils redissonUtils;


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String getCurrentDate() {
        return sdf.format(new Date());
    }


    @GetMapping("/test")
    public String getTest() {
        return "success";
    }

    @RequestMapping(value = "/testLock", method = RequestMethod.GET)
    public String testLock(String name) {

        final String lockKey = "lockKey";

        new Thread(() -> {

            redissonUtils.lock(lockKey, TimeUnit.MINUTES, 10);

            System.out.println(getCurrentDate() + " " + name + " begin...");
            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println(getCurrentDate() + " " + name + " " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(getCurrentDate() + " " + name + " end...");

            redissonUtils.unlock(lockKey);
        }).start();

        return "testlock";
    }

}
