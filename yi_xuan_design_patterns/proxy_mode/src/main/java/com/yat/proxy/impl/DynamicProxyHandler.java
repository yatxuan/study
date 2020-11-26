package com.yat.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>Description: 编写动态处理器 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 15:59
 */
public class DynamicProxyHandler implements InvocationHandler {

    private Object object;

    public DynamicProxyHandler(final Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("买房前准备");
        Object result = method.invoke(object, args);
        System.out.println("买房后装修");
        return result;
    }
}
