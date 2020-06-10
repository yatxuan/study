package com.yat;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;


/**
 * <p>Description: 压缩图片大小，不改变图片尺寸 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/3
 * @Time: 11:13
 */
public class ImageCompress {

    /**
     * 压缩后的图片路径
     */
    private final static String COMPRESSION_PATH = "D:\\Work\\Idea\\yasuo.jpg";
    /**
     * 图片低于多少则不压缩，默认300KB
     */
    private final static long DES_FILE_SIZE = 300 * 1024L;


    public static void main(String[] args) throws Exception {

        // 开始时间
        long beginTime = System.currentTimeMillis();
        String path = "D:\\Work\\Idea\\123456.jpg";

        String compression = getCompression(path);
        System.out.println(compression);

        long timeConsume = (System.currentTimeMillis() - beginTime);

        System.out.println(" time:" + timeConsume + "毫秒");
        System.out.println(" time:" + timeConsume / 1000 + "秒");

    }

    /**
     * 压缩图片
     *
     * @param path 等待压缩的图片路径
     * @return /
     * @throws Exception /
     */
    public static String getCompression(String path) throws Exception {

        byte[] bytes = FileUtils.readFileToByteArray(new File(path));
        bytes = compressPicForScale(bytes);
        FileUtils.writeByteArrayToFile(new File(COMPRESSION_PATH), bytes);
        return COMPRESSION_PATH;
    }


    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes 源图片字节数组
     * @return 压缩质量后的图片字节数组
     */
    private static byte[] compressPicForScale(byte[] imageBytes) throws Exception {


        // 图片小于300kb,则不压缩
        if (imageBytes == null || imageBytes.length < DES_FILE_SIZE) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / 1024);
        while (imageBytes.length > DES_FILE_SIZE) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
            Thumbnails.of(inputStream)
                    .scale(accuracy)
                    .outputQuality(accuracy)
                    .toOutputStream(outputStream);
            imageBytes = outputStream.toByteArray();
        }
        System.out.println("【图片压缩】imageId=" + "x" + " | 图片原大小=" + srcSize / 1024 + "kb | 压缩后大小=" + imageBytes.length / 1024 + "kb");
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

}
