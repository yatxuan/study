package com.yat.models.service.impl;

import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yat.common.constant.UserConstant;
import com.yat.common.exception.BadRequestException;
import com.yat.common.exception.CustomException;
import com.yat.common.utils.ResultResponse;
import com.yat.common.utils.StringUtils;
import com.yat.common.utils.ip.AddressUtils;
import com.yat.config.redis.RedisUtils;
import com.yat.config.shiro.jwt.JwtUtil;
import com.yat.config.shiro.realm.ShiroRealm;
import com.yat.models.entity.UserEntity;
import com.yat.models.entity.dto.authority.LoginUser;
import com.yat.models.service.ILoginService;
import com.yat.models.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.yat.common.constant.CommonConstant.TOKEN;
import static com.yat.common.constant.JwtConstant.ONLINE_USER_LOGIN_TIMES;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/6/20 - 19:41
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements ILoginService {

    private final JwtUtil jwtUtil;
    private final IUserService userService;
    private final RedisUtils<String> redisUtils;
    private final ShiroRealm shiroRealm;

    @Override
    public ResultResponse login(String username, String password, int squeeze, HttpServletRequest request) {

        UserEntity user = userService.getOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUsername, username),
                false
        );

        // 账号不存在、密码错误  当前加密的盐为：用户的账号
        if (null == user || !StringUtils.equals(user.getPassword(),
                new Sha256Hash(password, user.getUsername()).toHex())) {
            throw new BadRequestException("账号或密码不正确");
        }

        //账号锁定
        if (UserConstant.STATUS_DISABLE.equals(user.getEnabled())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // 获取登陆的浏览器名称
        String browser = AddressUtils.getBrowser(request);
        // 获取当前客户端的ip
        String logIp = AddressUtils.getIpAddr(request);
        // 获取登录地点
        String address = AddressUtils.getCityInfo(logIp);
        //
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        String loginTime = simpleDateFormat.format(System.currentTimeMillis());

        LoginUser loginUser = LoginUser.builder()
                .username(user.getUsername())
                .nickName(user.getNickName())
                .logIp(logIp)
                .address(address)
                .browser(browser)
                .loginTime(loginTime)
                .logNumber(user.getLogNumber())
                .squeeze(squeeze)
                .build();


        // 生成令牌
        String token = jwtUtil.createToken(loginUser);

        ResultResponse response = ResultResponse.success();
        response.put(TOKEN, token);
        response.put("user", loginUser);
        return response;
    }

    @Override
    public void logout(HttpServletRequest request) {

        // 获取token
        String token = jwtUtil.getToken(request);
        // 获取当前登陆的用户名
        String userName = jwtUtil.parseJwt(token).getSubject();
        // 获取当前客户端的ip
        String logIp = AddressUtils.getIpAddr(request);

        // 删除这个账号的在线人数,客户端ip
        redisUtils.listRemove(ONLINE_USER_LOGIN_TIMES + userName, 1, logIp);
        // 删除用户登陆信息
        String onlineKey = jwtUtil.getOnlineKeyPrefix() + StringUtils.remove(logIp, ".") + userName;
        redisUtils.del(onlineKey);
        // 清楚shiro的session缓存
        shiroRealm.clearUserCache();

    }

    public static void main(String[] args) {
        System.out.println(new Sha256Hash("admin", "admin").toHex());
    }
}
