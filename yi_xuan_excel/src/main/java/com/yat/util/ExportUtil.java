package com.yat.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:24
 */
@Slf4j
public class ExportUtil {

    public static void main(String[] args) {
        String str = null;
        System.out.println(StringUtils.equals(str,null));
    }

    /**
     * 把数据内容写入指定的文件中
     *
     * @param content 内容
     */
    public static void write(String content) {
        try {
            File file = new File("D:\\lll.html");
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                System.out.println("文件创建：" + newFile);
            }
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            String regex = "^\\d*\\.";
            Pattern p = Pattern.compile(regex);
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {

                Matcher m = p.matcher(s);
                if (m.find()) {
                    s = String.format("<h1 style='mso-outline-level:1;'>" +
                            "<b>" +
                            "<span style='mso-spacerun:'yes';font-family:宋体;mso-ascii-font-family:Calibri;mso-hansi-font-family:Calibri;mso-bidi-font-family:'Times New Roman';font-weight:bold;font-size:10.0000pt;mso-font-kerning:22.0000pt;'>" +
                            "<font face='宋体'>%s</font>" +
                            "</span>" +
                            "</b>" +
                            "<b>" +
                            "<span style='mso-spacerun:'yes';font-family:宋体;mso-ascii-font-family:Calibri;mso-hansi-font-family:Calibri;mso-bidi-font-family:'Times New Roman';font-weight:bold;font-size:10.0000pt;mso-font-kerning:22.0000pt;'>" +
                            "<o:p></o:p>" +
                            "</span>" +
                            "</b>" +
                            "</h1>", s);
                }

                result.append(System.lineSeparator()).append(s);
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public void exportWord(HttpServletRequest request, HttpServletResponse response, String content) {

        try {

            //这里是必须要设置编码的，不然导出中文就会乱码。
            byte[] b = content.getBytes("GB2312");
            //将字节数组包装到流中
            ByteArrayInputStream bais = new ByteArrayInputStream(b);

            /*
              关键地方
              生成word格式
             */
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
            // 输出文件
            String fileName = "wordFileName";
            request.setCharacterEncoding("GB2312");
            // 导出word格式
            response.setContentType("application/msword");
            response.setHeader("Content-disposition", "attachment; filename=临床科室N2题库.doc");

            OutputStream oStream = response.getOutputStream();
            poifs.writeFilesystem(oStream);
            bais.close();
            oStream.close();
        } catch (Exception e) {
            log.error("导出出错：{}", e.getMessage());
        }
    }

}
