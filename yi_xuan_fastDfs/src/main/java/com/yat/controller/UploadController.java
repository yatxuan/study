package com.yat.controller;

import com.yat.utils.FastDfsClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/5
 * @Time: 14:07
 */
@Slf4j
@RestController
public class UploadController {

    @Autowired
    private FastDfsClientUtil dfsClient;

    /**
     * 上传图片
     *
     * @param file 图片
     * @return 图片访问地址
     */
    @PostMapping("/upload/Image")
    public String fastDfsUploadImage(@RequestParam("file") MultipartFile file) {

        try {
            return dfsClient.uploadFileImage(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 图片访问地址
     */
    @PostMapping("/upload")
    public String fastDfsUpload(@RequestParam("file") MultipartFile file) {

        try {
            return dfsClient.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 下载图片
     *
     * @param filePath - http://192.168.1.138:8888/group1/M00/00/00/wKgBil5gup-AOBTfAAClFitxlGY465.jpg
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download")
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {

        //    group1/M00/00/00/wKgIZVzZEF2ATP08ABC9j8AnNSs744.jpg
        String[] paths = filePath.split("/");
        String groupName = null;
        for (String item : paths) {
            if (item.contains("group")) {
                groupName = item;
                break;
            }
        }
        String path = filePath.substring(filePath.indexOf(groupName) + groupName.length() + 1);
        InputStream input;
        try {
            input = dfsClient.download(groupName, path);

        } catch (Exception e) {
            log.error("下载的文件不存在--------------------------->{}", e.getMessage());
            return;
        }
        //根据文件名获取 MIME 类型
        String fileName = paths[paths.length - 1];
        // wKgIZVzZEF2ATP08ABC9j8AnNSs744.jpg
        log.info("下载的文件：fileName --->{}", fileName);
        String contentType = request.getServletContext().getMimeType(fileName);
        String contentDisposition = "attachment;filename=" + fileName;

        // 设置头
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Disposition", contentDisposition);

        // 获取绑定了客户端的流
        ServletOutputStream output = response.getOutputStream();

        // 把输入流中的数据写入到输出流中
        IOUtils.copy(input, output);
        input.close();

    }

    /**
     * @param filePath http://192.168.1.138:8888/group1/M00/00/00/wKgBil5gv4aAJnmCAACHmWFv_Wg172.jpg
     * @return 删除的文件名称
     */
    @RequestMapping("/deleteFile")
    public String delFile(String filePath) {

        try {
            dfsClient.delFile(filePath);
        } catch (Exception e) {
            // 文件不存在报异常 ： com.github.tobato.fastdfs.exception.FdfsServerException:
            // 错误码：2，错误信息：找不到节点或文件
            log.error("删除的文件不存在----------------->{}", e.getMessage());
        }
        return "成功删除，'" + filePath;

    }

}
