package com.yat.test;

import com.yat.excel.annotation.Excel;
import com.yat.model.PhoneModel;
import com.yat.util.ExcelParam;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/29 - 18:07
 */
public class ExcelTest {

    public static void main(String[] args) throws Exception {
        Field field;
        List<String> list = new ArrayList<>(16);
        List<PhoneModel> listM = null;
        // 文件名称
        String fileName = "测试Excel导出";
        HttpServletResponse response = null;
        ExcelParam excelParam = new ExcelParam(PhoneModel.class,  response, fileName, listM);
        Class clazz = excelParam.getClazz();
        System.out.println(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field value : fields) {
            System.out.println(value.getName());
        }
        for (Field value : fields) {
            field = clazz.getDeclaredField(value.getName());
            Excel column = field.getAnnotation(Excel.class);
            if (null != column) {
                list.add(column.title());
            }
        }

        list.forEach(System.out::println);
    }

}
