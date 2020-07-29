package com.yat.util;


/**
 * <p>Description: 字符串工具类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:46
 */
public class StringUtil {

    /**
     * 字符串首字母转小写
     *
     * @param s 、
     * @return 、
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder())
                    .append(Character.toLowerCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    /**
     * 字符串首字母转大写
     *
     * @param s 、
     * @return 、
     */
    public static String toUpperCaseFirstOne(String s) {

        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder())
                    .append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    /**
     * @param strSource
     * @param strFrom
     * @param strTo
     * @return java.lang.String
     * @Function 替换字符串
     * @author likaixuan
     * @Date 2019-07-05 15:06
     */
    public static String replace(String strSource, String strFrom,
                                 String strTo) {

        // 如果要替换的子串为空，则直接返回源串
        if (strFrom == null || "".equals(strFrom)) {
            return strSource;
        }
        String strDest = "";
        // 要替换的子串长度
        int intFromLen = strFrom.length();
        int intPos;
        // 循环替换字符串
        while ((intPos = strSource.indexOf(strFrom)) != -1) {
            // 获取匹配字符串的左边子串
            strDest = strDest + strSource.substring(0, intPos);
            // 加上替换后的子串
            strDest = strDest + strTo;
            // 修改源串为匹配子串后的子串
            strSource = strSource.substring(intPos + intFromLen);
        }
        // 加上没有匹配的子串
        strDest = strDest + strSource;
        // 返回
        return strDest;
    }

}

