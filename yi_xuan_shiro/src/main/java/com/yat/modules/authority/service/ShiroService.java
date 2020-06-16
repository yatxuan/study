package com.yat.modules.authority.service;

import com.yat.modules.authority.dto.LoginUser;
import com.yat.modules.entity.UserEntity;

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
public interface ShiroService {


    /**
     * 获取当前登陆的用户
     *
     * @param request /
     * @return /
     */
    LoginUser getCurrUser(HttpServletRequest request);


    /**
     * 虽然shiro在 BasicHttpAuthenticationFilter 类里面已经有了获取请求头token的方法（getAuthzHeader）
     * 但是，这里我们自定义获取请求头里的token方法
     * 除了要验证token是否存在，还要验证token是否正确
     *
     * @param request /
     * @return /
     */
    String getToken(HttpServletRequest request);


    /**
     * 查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    UserEntity findUserByUserName(String userName);

}
