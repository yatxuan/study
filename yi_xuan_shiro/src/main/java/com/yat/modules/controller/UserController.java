package com.yat.modules.controller;
import java.util.Date;

import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.yat.common.utils.ResultResponse;
import com.yat.config.properties.RsaSecret;
import com.yat.modules.entity.UserEntity;
import com.yat.modules.from.LoginFrom;
import com.yat.modules.service.IUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

/**
 * <p>Description: 系统用户 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/16
 * @Time: 15:48
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final RsaSecret rsaSecret;
    private final IUserService userService;


    @PostMapping
    public ResultResponse save(@RequestBody UserEntity loginFrom) {

        // 密码解密
        String username = rsaSecret.getDecrypt(loginFrom.getUsername());
        String password = rsaSecret.getDecrypt(loginFrom.getPassword());

        loginFrom.setUsername(username);
        loginFrom.setPassword(new Sha256Hash(password, username).toHex());

        userService.save(loginFrom);
        return ResultResponse.success();
    }

    @PutMapping
    public ResultResponse update( ) {

        UserEntity loginFrom =new UserEntity();
        loginFrom.setNickName("123");
        loginFrom.setId(1272819601889124353L);
        userService.updateById(loginFrom);
        return ResultResponse.success();
    }

    @DeleteMapping
    public ResultResponse del(){
        userService.removeById(1272819601889124353L);
        return ResultResponse.success();
    }

    @GetMapping("/rsa")
    public ResultResponse encrypt(String data) {
        return ResultResponse.success(rsaSecret.getEncrypt(data));

    }


    public static void main(String[] args) {
        RSA rsa = new RSA(AsymmetricAlgorithm.RSA.getValue());
    }
}
