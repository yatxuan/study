package com.yat;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/23 - 11:00
 */
public class Test {

    public static void main(String[] args) {
        Date date = new Date();

        //当前时间字符串，格式：yyyy-MM-dd HH:mm:ss
        String now = DateUtil.now();
        System.out.println(now);

        DateTime dateTime = DateUtil.offsetMinute(date, 1);
        System.out.println(dateTime.toString());

    }
}
