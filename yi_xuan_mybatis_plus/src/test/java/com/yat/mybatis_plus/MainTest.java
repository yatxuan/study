package com.yat.mybatis_plus;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/20
 * @Time: 16:59
 */
public class MainTest {

    public static void main(String[] args) {
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        System.out.println(cal.get(Calendar.DAY_OF_WEEK));
        System.out.println(w);
        System.out.println(weekDays[w]);
    }
}
