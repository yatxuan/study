package com.yat.modules.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yat.common.constant.CommonConstant;
import com.yat.common.constant.UserConstant;
import com.yat.common.exception.BadRequestException;
import com.yat.common.utils.AddressUtils;
import com.yat.common.utils.ResultResponse;
import com.yat.common.utils.StringUtils;
import com.yat.config.shiro.jwt.JwtUtil;
import com.yat.modules.authority.dto.LoginUser;
import com.yat.modules.authority.service.ILoginService;
import com.yat.modules.authority.service.ShiroService;
import com.yat.modules.entity.UserEntity;
import com.yat.modules.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/31
 * @Time: 14:48
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements ILoginService {

    private final JwtUtil jwtUtil;
    private final IUserService userService;

    @Override
    public ResultResponse login(String username, String password, HttpServletRequest request) {

        UserEntity user = userService.getOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUsername, username)
        );

        //账号不存在、密码错误  当前加密的盐为：用户的账号
        if (null == user || !StringUtils.equals(user.getPassword(),
                new Sha256Hash(password, user.getUsername()).toHex())) {
            throw new BadRequestException("账号或密码不正确");
        }

        //账号锁定
        if (UserConstant.STATUS_DISABLE.equals(user.getEnabled())) {
            throw new BadRequestException("账号已被锁定,请联系管理员");
        }

        // 获取ip
        String logIp = AddressUtils.getIpAddr(request);
        LoginUser loginUser = LoginUser.builder()
                .username(user.getUsername())
                .nickName(user.getNickName())
                .logIp(logIp)
                .browser(AddressUtils.getBrowser(request))
                .address(AddressUtils.getCityInfo(logIp))
                .loginTime(new Date())
                .build();

        // 生成令牌
        String token = jwtUtil.createToken(loginUser);

        ResultResponse response = ResultResponse.success();
        response.put(CommonConstant.TOKEN, token);
        response.put("user", loginUser);
        return response;
    }
}
