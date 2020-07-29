package com.yat.model;

import com.yat.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * <p>Description: 实体类 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/29 - 14:18
 */
@Data
public class PhoneModel implements Serializable {

    private static final long serialVersionUID = -8359152769434985735L;

    @Excel(title = "序号")
    private int num;

    @Excel(title = "手机名称")
    private String phoneName;

    @Excel(title = "颜色")
    private String color;

    @Excel(title = "售价")
    private double price;

    @Excel(title = "时间")
    private Date sj;

    /**
     * 解析字段注解
     *
     * @param clazz 、
     */
    public static <T> void parseField(Class<T> clazz) throws Exception {

        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (Field value : fields) {
            value.setAccessible(true);
        }
        for (Field value : fields) {
            field = clazz.getDeclaredField(value.getName());
            Excel column = field.getAnnotation(Excel.class);
            if (column != null) {
                System.out.println(value.getName() + ":" + column.title());
            }
        }
    }
}
