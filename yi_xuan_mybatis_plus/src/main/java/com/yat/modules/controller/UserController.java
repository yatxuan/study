package com.yat.modules.controller;

import com.yat.modules.entity.UserEntity;
import com.yat.modules.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Description: 描述 </p>
 * <p>RequiredArgsConstructor:会自动通过构造注入，final的bean</p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 16:18
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/list")
    public List<UserEntity> getUserList() {
        return userService.list();
    }
}
