package com.yat.common.constant;

import io.jsonwebtoken.Claims;

/**
 * <p>Description: 常用静态常量 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/30
 * @Time: 16:36
 */
public interface CommonConstant {

    String RESET_PASS = "重置密码";

    String RESET_MAIL = "重置邮箱";

    /**
     * 用于IP定位转换
     */
    String REGION = "内网IP|内网IP";

    /**
     * 限流标识
     */
    String LIMIT_ALL = "YAT_LIMIT_ALL";

    /**
     * 通用成功标识
     */
    String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    String FAIL = "1";

    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    String TOKEN = "token";

    /**
     * 令牌前缀
     */
    String TOKEN_PREFIX = "Bearer ";
    /**
     * 令牌的key
     */
    String HEADER = "Authorization";

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    String BASE_64_SECRET = "ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";
    /**
     * 用户ID
     */
    String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    String RESOURCE_PREFIX = "/profile";


    /**
     * 空值常量
     */
    String NULL_VALUE = " ";

    /**
     * 常量 0
     */
    String NUM_ZERO = "0";
}
