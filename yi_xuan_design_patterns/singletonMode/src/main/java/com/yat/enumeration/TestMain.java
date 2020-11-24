package com.yat.enumeration;

/**
 * <p>Description: 枚举调用方法 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/24 - 16:15
 */
public class TestMain {

    public static void main(String[] args) {
        Resource instance = SingletonEnum.INSTANCE.getInstance();
        System.out.println(instance);
    }
}
