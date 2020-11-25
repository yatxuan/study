package com.yat;

/**
 * <p>Description: 懒汉式:顾名思义就是实例在用到的时候才去创建，“比较懒”，
 * 用的时候才去检查有没有实例，如果有则返回，没有则新建。
 * 有线程安全和线程不安全两种写法，区别就是synchronized关键字。 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/24 - 15:54
 */
public class LazyMan {

    private static LazyMan instance;

    private LazyMan() {
        System.err.println("--------进入LazyMan构造方法----------");
    }

    public static LazyMan getInstance() {
        if (null == instance) {
            instance = new LazyMan();
        }
        return instance;
    }
}
