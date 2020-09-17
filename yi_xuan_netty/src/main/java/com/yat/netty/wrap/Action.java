package com.yat.netty.wrap;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:28
 */
@Data
@SuppressWarnings("all")
public class Action {
    String httpMethod;
    Class<?> controllerClass;
    Method method;
    String[] argNames;
    Object bean;
    String responseType;
   // AbstractActionIntercptor interceptor;

    public Action() {
    }

    /**
     * @param httpMethod
     * @param controllerClass
     * @param method
     * @param argNames
     */
    public Action(String httpMethod, Class<?> controllerClass, Method method, String[] argNames) {
        super();
        this.httpMethod = httpMethod;
        this.controllerClass = controllerClass;
        this.method = method;
        this.argNames = argNames;
    }

    public Action(String httpMethod, Class<?> controllerClass, Method method, String[] argNames,Object bean) {
        super();
        this.httpMethod = httpMethod;
        this.controllerClass = controllerClass;
        this.method = method;
        this.argNames = argNames;
        this.bean = bean;
    }

    public Action(String httpMethod, Class<?> controllerClass, Method method, String[] argNames,Object bean, String responseType) {
        super();
        this.httpMethod = httpMethod;
        this.controllerClass = controllerClass;
        this.method = method;
        this.argNames = argNames;
        this.bean = bean;
        this.responseType = responseType;
    }
}
