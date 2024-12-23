package com.yat.controller;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.yat.entity.bo.FileVo;
import com.yat.service.FastDfsClientUtil;
import com.yat.utils.fastdfs.Base64DecodedMultipartFile;
import com.yat.utils.fastdfs.FastDfsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
@RequiredArgsConstructor
public class UploadController {

    private final FastDfsClientUtil dfsClient;

    /**
     * 上传文件
     *
     * @param file         文件
     * @param isThumbnails 是否包含略缩图
     * @return 文件访问地址
     */
    @PostMapping("/upload")
    public FastDfsResponse fastDfsUpload(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "isThumbnails", required = false, defaultValue = "false") boolean isThumbnails) {
        return dfsClient.uploadFile(file, isThumbnails);
    }

    @PostMapping("/upload/base64")
    public FastDfsResponse fastDfsUpload(@RequestBody FileVo fileVo) {
        MultipartFile multipartFile = Base64DecodedMultipartFile.base64ToMultipart(fileVo.getImageBase());
        if (multipartFile == null) {
            log.error("文件上传失败！！！！！！！！！");
            return null;
        }
        return dfsClient.uploadFile(multipartFile, fileVo.isThumbnails());
    }

    /**
     * 下载文件
     *
     * @param filePath 文件的访问路径：例 - http://192.168.1.138:8888/group1/M00/00/00/wKgBil5gup-AOBTfAAClFitxlGY465.jpg
     * @param request  、
     * @param response 、
     */
    @GetMapping("/download")
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) {

        //    group1/M00/00/00/wKgIZVzZEF2ATP08ABC9j8AnNSs744.jpg
        String[] paths = filePath.split("/");
        String groupName = null;
        // 文件名
        String fileName = paths[paths.length - 1];
        for (String item : paths) {
            if (item.contains("group")) {
                groupName = item;
                break;
            }
        }

        assert groupName != null;
        String path = filePath.substring(filePath.indexOf(groupName) + groupName.length() + 1);

        try (InputStream input = dfsClient.download(groupName, path)) {

            //根据文件名获取 MIME 类型
            fileName = "Yat_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + StringUtils.substring(fileName, StringUtils.indexOf(fileName, "."));

            // 设置头
            response.setHeader("Content-Type", request.getServletContext().getMimeType(fileName));
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            // 获取绑定了客户端的流
            ServletOutputStream output = response.getOutputStream();
            // 把输入流中的数据写入到输出流中
            IOUtils.copy(input, output);

        } catch (FdfsServerException | IOException e) {
            log.error("下载的文件:'{}',不存在--------------------------->{}", fileName, e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param map: filePath 文件的访问路径：例：http://192.168.1.138:8888/group1/M00/00/00/wKgBil5gv4aAJnmCAACHmWFv_Wg172.jpg
     * @return 文件的访问路径
     */
    @DeleteMapping("/deleteFile")
    public String delFile(@RequestBody Map<String, String> map) {

        String filePath = map.get("filePath");
        try {
            dfsClient.delFile(filePath);
        } catch (FdfsServerException e) {
            log.error("删除的文件不存在----------------->{}", e.getMessage());
        }
        return "成功删除，'" + filePath;

    }


    /**
     * 查看文件
     *
     * @param filePath 文件的访问路径：例：http://192.168.1.138:8888/group1/M00/00/00/wKgBil5gv4aAJnmCAACHmWFv_Wg172.jpg
     * @return 、
     */
    @GetMapping("/query")
    public FileInfo query(String filePath) {
        try {
            return dfsClient.queryFileInfo(filePath);
        } catch (FdfsServerException e) {
            log.error("文件不存在：'{}'", e.getMessage());
            return null;
        }
    }

}
