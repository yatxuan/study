package com.yat.enumeration;

/**
 * <p>Description: 枚举单例模式 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/24 - 16:15
 */
public enum SingletonEnum {

    /**
     *
     */
    INSTANCE;

    private Resource instance;

    SingletonEnum() {
        instance = new Resource();
    }

    public Resource getInstance() {
        return instance;
    }
}
