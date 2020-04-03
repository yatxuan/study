package com.yat.utils.convert;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 十六进制字符串与字节数组的转换
 * 十六进制(hex):0-9,A-F组成,不区分大小写,与十进制对应关系：0-9对应0-9,A-F对应10-15
 * 位(bit):java语言中1个bit  用0或1表示
 * 字节(byte):java语言中1个byte=8个bit,1个bit=1个二进制位
 * 十进制:10-->二进制:1010-->十六进制:A
 * 1 hex = 4 bit; 1 byte = 8 bit --> 1 byte = 2 hex
 * 即1个字节需要用2个十六进制字符表示
 *
 * @author YatXuan
 * @date 2019-03-08 10:50:53
 */
public class HexConverter {

    /**
     * 十六进制字符
     */
    private static char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 字节数组 转 十六进制字符串
     *
     * @param bytes   字节数组
     * @param toUpper
     * @return 十六进制字符串, 默认字母是小写
     */
    public static String byteArrayToHexString(byte[] bytes, boolean toUpper) {
        if (ArrayUtils.isNotEmpty(bytes)) {
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                String hexStr = Integer.toHexString(b & 0xFF);
                if (hexStr.length() < 2) {
                    builder.append(0);
                }
                builder.append(hexStr);
            }
            return toUpper ? builder.toString().toUpperCase() : builder.toString();
        }
        return null;
    }

    /**
     * 十六进制字符串，转单个字节(取最后两位字符)
     * "1A"=26,"11A"=26,"11110"=16
     *
     * @param hexStr 十六进制字符串
     * @return
     */
    public static byte hexStringToByte(String hexStr) {
        return (byte) Integer.parseInt(hexStr, 16);
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexStr 十六进制字符串
     * @return
     */
    public static byte[] hexStringToByteArray(String hexStr) {
        byte[] bytes = null;
        if (isHexStr(hexStr)) {
            int length = hexStr.length() / 2;
            bytes = new byte[length];
            for (int i = 0; i < length; i++) {
                bytes[i] = (byte) Integer.parseInt(hexStr.substring(i * 2, (i + 1) * 2), 16);
            }
        }
        return bytes;
    }

    /**
     * 判断是否为十六进制字符
     *
     * @param c 十六进制字符
     * @return
     */
    public static boolean isHexChar(char c) {
        for (char hChar : hexChars) {
            if (c == hChar) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为十六进制字符串
     *
     * @param hexStr 十六进制字符串
     * @return
     */
    public static boolean isHexStr(String hexStr) {
        if (StringUtils.isNotEmpty(hexStr)) {
            char[] hexChars = hexStr.toCharArray();
            if (hexChars.length % 2 != 0) {
                System.out.println(hexStr + "[奇数位十六进制字符串]");
                return false;
            }
            for (char hexChar : hexChars) {
                if (!isHexChar(hexChar)) {
                    return false;
                }
            }
        }
        return true;
    }
}
