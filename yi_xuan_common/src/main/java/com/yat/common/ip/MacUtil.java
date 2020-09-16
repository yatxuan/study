package com.yat.common.ip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * <p>Description: 获取本机ip地址 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/16 - 17:48
 */
public class MacUtil {

    public static void main(String[] args) {
        System.out.println(getMacAddress("192.168.204.1"));
    }

    /**
     * 获取本机ip地址对应的Mac地址
     *
     * @param host 本机ip（公网ip，无法拿到对应的Mac地址）
     * @return Mac地址
     */
    public static String getMacAddress(String host) {
        String mac;
        StringBuilder sb = new StringBuilder();
        try {
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(host));
            byte[] macs = ni.getHardwareAddress();
            for (byte b : macs) {
                mac = Integer.toHexString(b & 0xFF);
                if (mac.length() == 1) {
                    mac = '0' + mac;
                }
                sb.append(mac).append("-");
            }
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        mac = sb.toString();
        mac = mac.substring(0, mac.length() - 1);

        return mac;
    }
}
