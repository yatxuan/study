package com.yat.utils.fastdfs;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * <p>Description: Base64图片转 MultipartFile</p>
 *
 * @author Yat-Xuan
 * @date 2020/8/24 19:44
 */
@Slf4j
public class Base64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;
    private final String header;

    private Base64DecodedMultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }

    @NonNull
    @Override
    public String getName() {
        return IdUtil.getSnowflake(1, 1).nextIdStr() + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return IdUtil.getSnowflake(1, 1).nextIdStr() + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @NonNull
    @Override
    public byte[] getBytes() {
        return imgContent;
    }

    @NonNull
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(@NonNull File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }


    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseSplit = StringUtils.split(base64, ",");

            if (baseSplit.length < 1) {
                // 如果当前数据内不包含文件格式：‘data:image/jpeg;base64’  直接返回
                log.error("缺少文件类型格式！！！！");
                return null;
            }

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(baseSplit[1]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new Base64DecodedMultipartFile(b, baseSplit[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
