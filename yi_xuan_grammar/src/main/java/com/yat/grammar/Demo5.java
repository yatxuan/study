package com.yat.grammar;

/**
 * <p>Description: 类构造器语法使用例子： </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/27 - 16:37
 */
public class Demo5 {

    public static void main(String[] args) {
        // 类构造器语法使用例子
        InterfaceDemo5 lamBer = Demo5::new;
        Demo5 lamBerDemo = lamBer.create();
        System.out.println(lamBerDemo);
    }
}


/**
 * <p>Description: 无参 -类构造器 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/27 16:18
 */
interface InterfaceDemo5 {
    /**
     * 无参 -类构造器
     *
     * @return 、
     */
    Demo5 create();

}
