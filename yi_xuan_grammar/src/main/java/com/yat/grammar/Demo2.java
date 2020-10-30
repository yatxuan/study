package com.yat.grammar;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Description: 对象实例语法	instanceRef::methodName </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/27 - 16:32
 */
public class Demo2 {

    public static void main(String[] args) {

        List<String> list = Arrays.asList("aaaa", "bbbb", "cccc");
        list.forEach(new Demo2()::getPrint);

    }

    private void getPrint(String str) {
        System.out.println(str);
    }
}
