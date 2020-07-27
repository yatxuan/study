package com.yat.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Description: 静态方法语法	ClassName::methodName </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/27 - 16:34
 */
public class Demo3 {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("aaaa", "bbbb", "cccc");
        list.forEach(Demo3::getPrint);

    }

    private static void getPrint(String str) {
        System.out.println(str);
    }

}

