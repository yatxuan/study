package com.yat.common.constant;

/**
 * <p>Description: 常用静态常量 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 - 13:48
 */
public interface CommonConstant {

    /**
     * 令牌
     */
    String TOKEN = "token";

    /**
     * 验证码 key
     */
    String CODE_KEY = "CODE:KEY:";


    /**
     * redis-OK
     */
    String OK = "OK";

    /**
     * redis过期时间，以秒为单位，一分钟
     */
    int EXRP_MINUTE = 60;

    /**
     * redis过期时间，以秒为单位，一小时
     */
    int EXRP_HOUR = 60 * 60;

    /**
     * redis过期时间，以秒为单位，一天
     */
    int EXRP_DAY = 60 * 60 * 24;

    /**
     * redis-key-前缀-shiro:cache:
     */
    String PREFIX_SHIRO_CACHE = "shiro:cache:";

    /**
     * shiro的用户信息
     */
    String AUTHENTICATION_CACHE = "authenticationCache:";
    /**
     * shiro的用户角色和权限
     */
    String AUTHORIZATION_CACHE = "authorizationCache:";

    /**
     * 用户缓存在redis的过期时间 默认 360S 单位毫秒
     */
    int EXPIRE = 60 * 60;

    /**
     * redis-key-前缀-shiro:access_token:
     */
    String PREFIX_SHIRO_ACCESS_TOKEN = "shiro:access_token:";

    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

    /**
     * JWT-account:
     */
    String ACCOUNT = "account";

    /**
     * JWT-currentTimeMillis:
     */
    String CURRENT_TIME_MILLIS = "currentTimeMillis";

    /**
     * PASSWORD_MAX_LEN
     */
    Integer PASSWORD_MAX_LEN = 8;
}
