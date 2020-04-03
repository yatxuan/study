package com.yat.utils.path;

import java.io.File;
import java.io.IOException;

/**
 * 路径工具类
 *
 * @author YatXuan
 * @date 2019-03-04 16:34:01
 */
public class PathUtil {

    /**
     * 获取项目路径
     *
     * @return
     */
    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取项目路径
     *
     * @return
     * @throws IOException
     */
    public static String getProjectPath2() throws IOException {
        // 参数为空
        File directory = new File("");
        return directory.getCanonicalPath();
    }

}
