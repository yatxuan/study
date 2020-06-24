package com.yat.models.service;

import com.yat.common.utils.ResultResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/6/20 - 19:41
 */
public interface ILoginService {

    /**
     * 用户登陆
     *
     * @param username 账号
     * @param password 密码
     * @param squeeze  1-挤调第一个地方的登陆，0-不挤掉，-1-返回报错提示
     * @param request  /
     * @return ResultResponse
     */
    ResultResponse login(String username, String password, int squeeze, HttpServletRequest request);


    /**
     * 退出登陆
     *
     * @param request 、
     */
    void logout(HttpServletRequest request);


}
