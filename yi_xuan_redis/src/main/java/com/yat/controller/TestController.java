package com.yat.controller;

import com.yat.config.RedisUtils;
import com.yat.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/6/30 - 18:26
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final RedisUtils<Object> redisUtils;

    @GetMapping("/set")
    public Object set() {

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("张三");
        userEntity.setPassWord("123456");
        userEntity.setAge(20);

        redisUtils.set("admin", userEntity);
        return true;
    }

    @GetMapping("/get")
    public Object get() {
        return redisUtils.get("admin");
    }

    @GetMapping("/get/user")
    public UserEntity getUser() {
        return (UserEntity) redisUtils.get("admin");
    }

    /**
     * 通过前缀删除 key
     */
    @GetMapping("/del")
    public void remove() {
        String a = "a";

        List<String> list = redisUtils.scan(a+"*");
        list.forEach(redisUtils::del);
    }
}
