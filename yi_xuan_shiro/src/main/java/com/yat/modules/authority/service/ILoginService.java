package com.yat.modules.authority.service;

import com.yat.common.utils.ResultResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/31
 * @Time: 14:48
 */
public interface ILoginService {

    /**
     * 登陆
     *
     * @param username 账号
     * @param password 密码
     * @param request /
     * @return ResultResponse
     */
    ResultResponse login(String username, String password, HttpServletRequest request);
}
