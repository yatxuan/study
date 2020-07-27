package com.yat.grammar;

import com.yat.entity.UserEntity;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Description: 对象的超类方法语法 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/27 - 16:35
 */
public class Demo4 extends BaseLamBer  {


    public static void main(String[] args) {
        new Demo4().getPrint();
    }

    private void getPrint() {
        List<String> list = Arrays.asList("aaaa", "bbbb", "cccc");
        // 对象的超类方法语法： super::methodName
        list.forEach(super::print);
    }
}


/**
 * <p>Description: 对象的超类方法语法 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/27 16:19
 */
class BaseLamBer {

    public void print(String content) {
        System.out.println(content);
    }
}
