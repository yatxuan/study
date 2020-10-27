package com.yat.mariadb.config.mybatis.xss;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: SQL过滤 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 16:08
 */
public class SqlFilter {

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        // 转换成大写
        str = StringUtils.upperCase(str);

        // 非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        // 判断是否包含非法字符
        for (String keyword : keywords) {
            if (StringUtils.containsIgnoreCase(str, keyword)) {
                throw new RuntimeException("包含非法字符");
            }
        }

        return str;
    }
}
