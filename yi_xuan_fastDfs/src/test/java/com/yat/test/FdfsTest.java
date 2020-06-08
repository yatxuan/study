package com.yat.test;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.yat.FastDfsApplication;
import com.yat.utils.FastDfsClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
@SpringBootTest(classes = FastDfsApplication.class)
public class FdfsTest {

    /**
     * 域名/IP
     */
    @Value("${fdfs.reqHost}")
    private String reqHost;

    /**
     * 端口
     */
    @Value("${fdfs.reqPort}")
    private String reqPort;

    private final FastFileStorageClient storageClient;
    private final ThumbImageConfig thumbImageConfig;
    private final FastDfsClientUtil fastDfsClientUtil;


    @Test
    public void testUpload() throws FileNotFoundException {
        File file = new File("D:\\Work\\Idea\\测试文本.txt");
        // 上传文件
        StorePath storePath = this.storageClient.uploadFile(
                new FileInputStream(file), file.length(), "txt", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
    }

    @Test
    public void testUploadAndCreateThumb() throws FileNotFoundException {
        File file = new File("D:\\Work\\Idea\\yi_xuan_study\\common\\pic\\WeChat.png");

        // 上传图片
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "png", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());

        // 图片访问URL
        System.out.println(getFilePath(storePath));
        // 略缩图图片访问URL
        System.out.println(getThumbnailsUrl(storePath));
    }

    @Test
    public void delImage() {
        String path = "http://accessimage.yatxuan.cn/group1/M00/00/00/rBKsVl6Gp7WAeOgJAAAQmDrLZTA452_150x150.jpg";
        fastDfsClientUtil.delFile(path);
    }

    /**
     * 获取文件的完整访问路径
     *
     * @param storePath 、
     * @return 、
     */
    private String getFilePath(StorePath storePath) {
        return getResAccessUrl(storePath.getFullPath());
    }

    /**
     * 获取略缩图
     *
     * @param storePath /
     * @return 访问略缩图的 URL
     */
    private String getThumbnailsUrl(StorePath storePath) {
        String path = thumbImageConfig.getThumbImagePath(storePath.getFullPath());
        log.info("略缩图：thumbImage--->{}", "http://" + reqHost + ":" + reqPort + "/" + path);
        return getResAccessUrl(path);
    }

    /**
     * 封装文件完整URL地址
     *
     * @param path /
     * @return 访问图片的 URL
     */
    private String getResAccessUrl(String path) {
        return "http://" + reqHost + ":" + reqPort + "/" + path;
    }
}
