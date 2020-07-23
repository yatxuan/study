package com.yat.reptile.util;

import javax.servlet.http.HttpServletResponse;

/**
 * 返回数据跨域配置
 *
 * @author Yat-Xuan
 * @since 2019/7/17 17:22
 */
public class CrossUtil {

    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,token,username,client";

    /**
     * 设置跨域的请求方式：GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS
     */
    private static final String ALLOWED_METHODS = "*";

    private static final String ALLOWED_ORIGIN = "*";

    private static final String ALLOWED_EXPOSE = "*";

    private static final String MAX_AGE = "18000L";

    /**
     * 配置请求头
     *
     * @param response 、
     */
    public static void setCrossHeader(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        response.setHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
        response.setHeader("Access-Control-Max-Age", MAX_AGE);
        response.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        response.setHeader("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

}
