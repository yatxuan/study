package com.yat.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>Description: 异常工具 </p>
 * @author Yat-Xuan
 * @date 2020/3/23 12:46
*/
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

}
