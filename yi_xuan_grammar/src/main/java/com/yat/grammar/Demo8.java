package com.yat.grammar;

import java.util.HashMap;

/**
 * <p>Description: 这里是借用jdk自带的java.util.function.Function类实现的，如果想要自定义接口 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/27 16:21
 */
public class Demo8 {

    public static void main(String[] args) {
        // 创建数组
        Interface<Integer, Demo8[]> function = Demo8[]::new;
        // 这里的4是数组的大小
        Demo8[] array = function.apply(4);

        for (Demo8 e : array) {
            System.out.println(e);
        }

        // 赋值
        array[0] = new Demo8();
        array[1] = new Demo8();
        array[2] = new Demo8();
        array[3] = new Demo8();
        for (Demo8 e : array) {
            System.out.println(e);
        }

        // 创建HashMap
        Interface<Integer, HashMap<String,String>> anInterface = HashMap::new;
        HashMap<String,String> apply = anInterface.apply(3);
        apply.put("key", "value");
        System.out.println(apply);
        System.out.println(apply.size());

    }
}

@FunctionalInterface
interface Interface<A, T> {
    /**
     * 自定义接口
     *
     * @param a 、
     * @return 、
     */
    T apply(A a);
}
