package com.yat.controller;

import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.yat.utils.FastDfsClientUtil;
import com.yat.utils.FastDfsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
@SuppressWarnings("all")
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
     * @return 文件访问地址
     */
    @PostMapping("/upload")
    public FastDfsResponse fastDfsUpload(@RequestParam("file") MultipartFile file) {
        try {
            return dfsClient.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 下载文件
     *
     * @param filePath 文件的访问路径：例 - http://192.168.1.138:8888/group1/M00/00/00/wKgBil5gup-AOBTfAAClFitxlGY465.jpg
     * @param request  、
     * @param response 、
     * @throws IOException 、
     */
    @GetMapping("/download")
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) {

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

        try (InputStream input = dfsClient.download(groupName, path)) {

            //根据文件名获取 MIME 类型
            String fileName = "Yat_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + StringUtils.substring(
                    paths[paths.length - 1], StringUtils.indexOf(paths[paths.length - 1], "."));

            // 设置头
            response.setHeader("Content-Type", request.getServletContext().getMimeType(fileName));
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            // 获取绑定了客户端的流
            ServletOutputStream output = response.getOutputStream();
            // 把输入流中的数据写入到输出流中
            IOUtils.copy(input, output);

        } catch (FdfsServerException e) {
            log.error("下载的文件不存在--------------------------->{}", e.getMessage());
        } catch (IOException e) {
            log.error("下载的文件不存在--------------------------->{}", e.getMessage());
        }
    }

    /**
     * @param filePath 文件的访问路径：例：http://192.168.1.138:8888/group1/M00/00/00/wKgBil5gv4aAJnmCAACHmWFv_Wg172.jpg
     * @return 文件的访问路径
     */
    @DeleteMapping("/deleteFile")
    public String delFile(@RequestBody Map<String, String> map) {

        String filePath = map.get("filePath");
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
