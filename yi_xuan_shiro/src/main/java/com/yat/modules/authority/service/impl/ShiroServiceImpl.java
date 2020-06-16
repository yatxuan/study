package com.yat.modules.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yat.common.utils.AddressUtils;
import com.yat.common.utils.StringUtils;
import com.yat.config.shiro.jwt.JwtUtil;
import com.yat.modules.authority.dto.LoginUser;
import com.yat.modules.authority.service.ShiroService;
import com.yat.modules.entity.UserEntity;
import com.yat.modules.service.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/31
 * @Time: 8:59
 */
@Slf4j
@Service("shiroService")
@RequiredArgsConstructor
public class ShiroServiceImpl implements ShiroService {


    private final JwtUtil jwtUtil;
    private final IUserService userService;


    @Override
    public LoginUser getCurrUser(HttpServletRequest request) {
        return jwtUtil.getLoginUser(request);
    }


    @Override
    public String getToken(HttpServletRequest request) {
        // 当前用户登陆的ip
        String ipAddr = AddressUtils.getIpAddr(request);

        // 当前请求的接口名称
        String requestUrl = request.getRequestURI();

        // 获取当前请求中的token
        final String token = jwtUtil.getToken(request);
        // 在线用户
        try {
            // 查看redis是否包含用户的登陆消息，判断token是否正确，时间是否过期
            if (StringUtils.isNotBlank(token) &&
                    org.springframework.util.StringUtils.hasText(token) &&
                    jwtUtil.validateToken(token)) {
                LoginUser currUser = getCurrUser(request);

                if (null == currUser) {
                    log.error("用户不存在，令牌错误：token: '{}'，请重新登录,uri: '{}',IP: '{}'", token, requestUrl, ipAddr);
                    return null;
                }
                log.info("set Authentication to security context for '{}', uri: '{}',IP: '{}'", currUser.getUsername(), requestUrl, ipAddr);
            } else {
                log.error("no valid JWT token found, uri: '{}',IP: '{}", requestUrl, ipAddr);
                return null;
            }
            return token;
        } catch (IllegalArgumentException |
                ExpiredJwtException e) {
            log.error("no valid JWT token found, uri: '{}',IP: '{}", requestUrl, ipAddr);
            return null;
        }
    }


    @Override
    public UserEntity findUserByUserName(String userName) {
        return userService.getOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUsername, userName)
        );
    }
}
