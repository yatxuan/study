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
import io.jsonwebtoken.Claims;
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

        // 获取当前客户端的ip
        String logIp = AddressUtils.getIpAddr(request);
        Date date = new Date();
        // 判断当前账号是否在其他客户端进行登陆
        if (redisUtils.hasKey(ONLINE_USER_LOGIN_TIMES + username)) {
            // 获取当前账号 客户端登陆地点的ip
            List<String> list = redisUtils.listRange(ONLINE_USER_LOGIN_TIMES + username);
            if (null != list && !list.isEmpty()) {
                // 判断用户是否为 重复登陆
                if (!list.contains(logIp)) {
                    // 如果不是重复登陆，判断该账号是否允许多地点登陆
                    // (这里可以到数据库查询，也可以到配置文件中设置，每个账号的同时登陆人数)
                    // 判断 允许的最大的登陆人数 小于等于 当前已经登录的人数
                    if (user.getLogNumber() <= list.size()) {
                        // 判断是否挤退别人
                        if (1 == squeeze) {
                            //  挤掉最先登录的用户: 获取当前账号，第一个客户端的ip
                            String fistLogIp = redisUtils.listGetIndex(ONLINE_USER_LOGIN_TIMES + username, 0);
                            String ip = StringUtils.remove(fistLogIp, ".");

                            // 挤掉最先登录的用户: 把当前账号的客户端的ip进行覆盖
                            redisUtils.listUpdateIndex(ONLINE_USER_LOGIN_TIMES + username, 0, logIp);
                            String onlineKey = jwtUtil.getOnlineKeyPrefix() + ip + username;
                            redisUtils.del(onlineKey);

                            // 在给退出的客户端一个友好提示
                            StrBuilder strBuilder = new StrBuilder("您的账号于");
                            strBuilder.append(new SimpleDateFormat("HH:mm").format(date));
                            strBuilder.append("在另一台设备上进行登陆，已经被迫下线，若非本人操作，请立即修改密码");
                            String friendlyTips = jwtUtil.getOnlineKeyPrefix() + username + ip;
                            redisUtils.set(friendlyTips, strBuilder.toString());

                        } else if (-1 == squeeze) {
                            throw new CustomException("当前您的账号在另一台设备上登陆，是否重新登陆？");
                        } else {
                            throw new CustomException("登陆失败！");
                        }
                    }
                } else {
                    log.info("重复登陆");
                }
            }
        }
        LoginUser loginUser = LoginUser.builder()
                .username(user.getUsername())
                .nickName(user.getNickName())
                .logIp(logIp)
                .browser(AddressUtils.getBrowser(request))
                .address(AddressUtils.getCityInfo(logIp))
                .loginTime(date)
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
