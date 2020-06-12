package com.yat;

import com.yat.common.refactoring.toolkit.StringUtils;

import java.io.File;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/12
 * @Time: 10:26
 */
public class FileTest {

    /*
     * File类的listFiles()列出当前目录下文件以及文件夹。
     *
     * 需求：能不能列出当前目录下的子目录中的所有内容。 思路： 1，在遍历当前目录时，会获取到当前的所有的文件以及文件夹，
     * 2，要遍历子目录需要对获取到的当前的file对象进行判断，只有是目录才可以作为子目录进行继续遍历。
     */
    public static void main(String[] args) {

        File dir = new File("D:\\BaiduNetdiskDownload\\谷粒商城");

        // 通过递归修改当前目录下，指定文件的名称
        listAll(dir);
    }

    public static void listAll(File dir) {

        // dir:接收目录以及子目录。
        System.out.println("dir:" + dir.getName());
        File[] files = dir.listFiles();
        if (null == files) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {// 如果遍历到当前的file对象是个目录，继续遍历。 info_wx.2020-06-10.log
                listAll(file);
            } else {
                boolean error = StringUtils.contains(file.getName(), "【瑞客论坛 www.ruike1.com】");
                if (error) {
                    String name = StringUtils.replace(file.getName(), "【瑞客论坛 www.ruike1.com】", "");
                    File newFile = new File(file.getParent() + File.separator + name);
                    file.renameTo(newFile);
                }
                System.out.println("file:" + file.getName());
            }
        }

    }

}
