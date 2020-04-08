package com.yat.social.oauth.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取IP的工具类
 *
 * @author Yat-Xuan
 * @since 1.0.0
 */
public class IpUtils {

    /**
     * 获取IP
     *
     * @return ip
     */
    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
