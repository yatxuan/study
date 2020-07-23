package com.yat;

import com.yat.file.Encode;

/**
 * <p>Description: 文件加密解密 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/9 - 11:07
 */
public class FileMain {

    public static void main(String[] args) {

        String filename = "D:\\01 谷粒商城的简介.avi";
        String key = "KEY1";
        new Encode(false, filename, key).run();
    }
}
