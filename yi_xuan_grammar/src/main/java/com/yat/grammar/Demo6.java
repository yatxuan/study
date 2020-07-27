package com.yat.grammar;

import lombok.AllArgsConstructor;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/27 - 16:39
 */
@AllArgsConstructor
public class Demo6 {

    private String name;

    public static void main(String[] args) {

        // 带参数的构造器，示例如下：
        InterfaceDemo6 com = Demo6::new;
        Demo6 bean = com.create("hello world");
        System.out.println(bean.name);
    }
}


/**
 * <p>Description: 带参数 -类构造器 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/27 16:18
 */
interface InterfaceDemo6 {
    /**
     * 带参数 -类构造器
     *
     * @return 、
     */
    Demo6 create(String name);
}
