package com.yat;

/**
 * <p>Description: 饿汉式:从名字上也很好理解，就是“比较勤”，
 * 实例在初始化的时候就已经建好了，不管你有没有用到，都先建好了再说。</p>
 * <p>优点：这种写法比较简单，就是在类装载的时候就完成实例化。避免了线程同步问题。</p>
 * <p>缺点：在类装载的时候就完成实例化，没有达到Lazy Loading的效果。如果从始至终从未使用过这个实例，则会造成内存的浪费。 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/24 - 15:56
 */
public class HungryChinese {

    private static HungryChinese instance = new HungryChinese();

    private HungryChinese() {
        System.err.println("--------进入HungryChinese构造方法----------");
    }

    public static HungryChinese getInstance() {
        return instance;
    }
}
