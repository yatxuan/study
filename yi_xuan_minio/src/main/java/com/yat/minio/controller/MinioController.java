package com.yat.minio.controller;

import com.yat.minio.model.MinioItem;
import com.yat.minio.model.MinioObject;
import com.yat.minio.service.MinioService;
import io.minio.messages.Bucket;
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
import java.util.List;
import com.yat.minio.common.utils.id.UUID;

/**
 * <p>Description: Minio文件 </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/13 17:05
 */
@Slf4j
@RestController
@RequestMapping("/minio-endpoint")
@SuppressWarnings("all")
public class MinioController {

    @Autowired
    private MinioService minioService;

    /**
     * 上传文件到minio服务
     *
     * @param multipartFile 、
     * @return 、
     */
    @PostMapping("/upload/{bucketName}")
    public MinioObject saveObject(@RequestParam("multipartFile") MultipartFile multipartFile,
                                  @PathVariable("bucketName") String bucketName) throws IOException {
        // 获取原文件名称
        String filename = multipartFile.getOriginalFilename();
        // 重新生成文件名，防止名称冲突
        String objectName = UUID.randomUUID().toString(true) + getSuffixName(filename);
        return minioService.saveObject(bucketName, objectName, multipartFile.getInputStream(), multipartFile.getSize(), multipartFile.getContentType());
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return /
     */
    @GetMapping("/stat/{bucketName}/{objectName}")
    public MinioObject statObject(@PathVariable String bucketName, @PathVariable String objectName) {
        return minioService.statObject(bucketName, objectName);
    }

    /**
     * 创建桶
     *
     * @param bucketName 存储桶名称
     * @return /
     */
    @PostMapping("/create/{bucketName}")
    public boolean createBucket(@PathVariable String bucketName) {
        return minioService.bucketExists(bucketName, true);
    }

    /**
     * 列出所有存储桶
     *
     * @return 、
     */
    @GetMapping("/buckets")
    public List<Bucket> listBuckets() {
        return minioService.listBuckets();
    }

    /**
     * 删除一个存储桶，如果该存储桶里有对象，默认不会直接删除存储桶
     *
     * @param bucketName 存储桶
     * @param insecure   true-强制删除 ，false-不删除（传参可用 0-false ，1-true  代替）
     */
    @DeleteMapping("/remove/{bucketName}/{insecure}")
    public void removeBucket(@PathVariable String bucketName, @PathVariable boolean insecure) {
        minioService.removeBucket(bucketName, insecure);
    }

    /**
     * 列出某个存储桶中的所有对象。
     *
     * @param bucketName 存储桶
     * @return 、
     */
    @GetMapping("/list/{bucketName}")
    public List<MinioItem> listObjects(@PathVariable String bucketName) {
        return minioService.listObjects(bucketName);
    }

    /**
     * 下载minio服务的文件
     *
     * @param bucketName 桶
     * @param objectName 文件名
     * @return /
     */
    @GetMapping("/download/{bucketName}/{objectName}")
    public String download(@PathVariable String bucketName, @PathVariable String objectName, HttpServletRequest request, HttpServletResponse response) {
        try (InputStream input = minioService.getObject(bucketName, objectName)) {
            //根据文件名获取 MIME 类型
            log.info("下载的原文件名：objectName --->{}", objectName);
            String fileName = "Yat_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + getSuffixName(objectName);
            String contentType = request.getServletContext().getMimeType(fileName);
            String contentDisposition = "attachment;filename=" + fileName;

            // 设置头
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Disposition", contentDisposition);

            // 获取绑定了客户端的流
            ServletOutputStream output = response.getOutputStream();

            // 把输入流中的数据写入到输出流中
            IOUtils.copy(input, output);
            return "下载完成";
        } catch (Exception e) {
            log.error("下载失败：{}", e.getMessage());
            return "下载失败";
        }
    }

    /**
     * 获取文件名的后缀
     *
     * @param fileName 文件名
     * @return 、
     */
    private String getSuffixName(String fileName) {
        int index = StringUtils.indexOf(fileName, ".");
        if (-1 == index) {
            return null;
        }
        return StringUtils.substring(fileName, index);
    }

}
