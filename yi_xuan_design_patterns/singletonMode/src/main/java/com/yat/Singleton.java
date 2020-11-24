package com.yat;

import java.io.Serializable;

/**
 * <p>Description: 静态内部类:静态内部类的方式效果类似双检锁，但实现更简单。
 * 但这种方式只适用于静态域的情况，双检锁方式可在实例域需要延迟初始化时使用。 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/24 - 16:00
 */
public class Singleton implements Serializable {

    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    private Singleton() {
        System.err.println("--------进入Singleton构造方法----------");
    }

    public static final Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
