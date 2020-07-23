package com.yat.reptile.rule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yat-Xuan
 * @since 2019/7/8
 */
public abstract class AbstractRule implements AntiReptileRule {


    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) {
        return doExecute(request, response);
    }

    /** IP访问、设备访问，拦截
     * @param request  、
     * @param response 、
     * @return 、
     */
    protected abstract boolean doExecute(HttpServletRequest request, HttpServletResponse response);
}
