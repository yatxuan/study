package com.yat;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/24 - 16:36
 */
public class Main {

    public static void main(String[] args) throws Exception {
        one();
        two();
        three();
        four();
        five();
    }

    /**
     * 饿汉式
     */
    private static void one() {
        System.out.println("-----------------饿汉式-----------------");
        HungryChinese instance = HungryChinese.getInstance();
        System.out.println(instance);
    }

    /**
     * 懒汉式
     */
    private static void two() {
        System.out.println("-----------------懒汉式-----------------");
        LazyMan instance = LazyMan.getInstance();
        System.out.println(instance);
    }

    /**
     * 双重校验锁
     */
    private static void three() throws Exception {
        System.out.println("-----------------双重校验锁-----------------");
        DoubleCheckLock instance = DoubleCheckLock.getInstance();
        System.out.println(instance);

        // 反序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("DoubleCheckLock.text")));
        //将对象进行写入
        oos.writeObject(instance);
        oos.close();
        //然后将对象写出
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("DoubleCheckLock.text"));
        DoubleCheckLock innerSingleton = (DoubleCheckLock) ois.readObject();
        ois.close();
        System.out.println("双重校验锁序列化后读取其中的内容：\n" + instance + "\n" + innerSingleton);

        // 反射破解
        Constructor<DoubleCheckLock> constructor = DoubleCheckLock.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        DoubleCheckLock newSingleton = constructor.newInstance();
        System.out.println(newSingleton);
        System.out.println(instance == newSingleton);
    }

    /**
     * 静态内部类
     */
    private static void four() throws Exception {
        System.out.println("-----------------静态内部类-----------------");
        Singleton instance = Singleton.getInstance();

        // 反序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Singleton.text")));
        //将对象进行写入
        oos.writeObject(instance);
        oos.close();
        //然后将对象写出
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Singleton.text"));
        Singleton innerSingleton = (Singleton) ois.readObject();
        ois.close();
        System.out.println("静态内部类序列化后读取其中的内容：\n" + instance + "\n" + innerSingleton);

        // 反射破解
        Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Singleton newSingleton = constructor.newInstance();
        System.out.println(newSingleton);
        System.out.println(instance == newSingleton);
    }

    /**
     * 枚举模式
     */
    private static void five() throws Exception {
        System.out.println("-----------------枚举模式-----------------");
        SingletonEnum instance = SingletonEnum.INSTANCE;
        System.out.println(instance);

        // 反序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("SingletonEnum.text")));
        //将对象进行写入
        oos.writeObject(instance);
        oos.close();
        //然后将对象写出
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SingletonEnum.text"));
        SingletonEnum innerSingleton = (SingletonEnum) ois.readObject();
        ois.close();
        System.out.println("枚举序列化：\n" + instance + "\n" + innerSingleton);
        System.out.println("枚举序列化后,读取其中的字段内容：\n" + instance.getInstance() + "\n" + innerSingleton.getInstance());

        // 反射破解 会抛出异常
        Constructor<SingletonEnum> constructor = SingletonEnum.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        SingletonEnum newSingleton = constructor.newInstance();
        System.out.println(newSingleton.getInstance());
    }
}
