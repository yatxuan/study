package com.yat.grammar;

import java.util.function.Function;

/**
 * <p>Description: 数组构造器语法使用例子 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/27 - 15:36
 */
public class Demo7 {

    public static void main(String[] args) {
        // 创建数组
        Function<Integer, Demo7[]> function = Demo7[]::new;
        // 这里的4是数组的大小
        Demo7[] array = function.apply(4);

        // 如果输出的话，你会发现会输出4个空对象(null)
        for (Demo7 e : array) {
            System.out.println(e);
        }

        // 赋值
        array[0] = new Demo7();
        array[1] = new Demo7();
        array[2] = new Demo7();
        array[3] = new Demo7();
        for (Demo7 e : array) {
            System.out.println(e);
        }
    }


}


