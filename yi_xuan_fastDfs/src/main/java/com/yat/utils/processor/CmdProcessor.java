package com.yat.utils.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * windows中 执行 cmd 或 bat
 *
 * @author YatXuan
 * @date 2020-01-03 11:07:48
 */
public class CmdProcessor {

    public static void main(String[] args) {
        //提交github
        callCmd("E:\\resources\\bat\\git_pull.bat");
        //生成内容，省略
        //提交github
        callCmd("E:\\resources\\bat\\git_push.bat");
    }

    /**
     * windows 调用 cmd 或 bat
     *
     * @param cmdFilePath
     */
    public static void callCmd(String cmdFilePath) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(cmdFilePath);
            InputStream is = p.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "GBK"));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
