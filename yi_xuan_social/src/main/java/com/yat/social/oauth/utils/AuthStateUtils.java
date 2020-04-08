package com.yat.social.oauth.utils;

/**
 * AuthState工具类，默认只提供一个创建随机uuid的方法
 *
 * @author Yat-Xuan
 * @since 1.9.3
 */
public class AuthStateUtils {

    /**
     * 生成随机state，采用https://github.com/lets-mica/mica的UUID工具
     *
     * @return 随机的state字符串
     */
    public static String createState() {
        return UuidUtils.getUUID();
    }
}
