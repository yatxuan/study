package com.yat.minio.service.impl;

import com.yat.minio.common.exception.CustomException;
import com.yat.minio.config.MinioConfig;
import com.yat.minio.model.MinioItem;
import com.yat.minio.model.MinioObject;
import com.yat.minio.service.MinioService;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/14
 * @Time: 13:57
 */
@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    private static MinioClient minioClient;

    static {
        try {
            minioClient = MinioConfig.getInstance();
        } catch (InvalidPortException | InvalidEndpointException e) {
            throw new CustomException("MinIoClient初始化失败");
        }
    }

    @Override
    public MinioObject saveObject(String bucketName, String objectName, InputStream stream,
                                  long size, String contentType) {
        try {
            bucketExists(bucketName, true);
            minioClient.putObject(bucketName, objectName, stream, size, contentType);
            return statObject(bucketName, objectName);
        } catch (XmlPullParserException e) {
            log.error("解析返回的XML异常{}", e.getMessage());
            throw new CustomException("解析返回的XML异常");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (InvalidBucketNameException e) {
            throw new CustomException("不合法的存储桶名称: '" + bucketName + "'");
        } catch (InsufficientDataException | InvalidArgumentException e) {
            throw new CustomException("Minio内部异常");
        } catch (NoResponseException e) {
            throw new CustomException("服务器无响应");
        } catch (ErrorResponseException e) {
            throw new CustomException("执行失败");
        } catch (InternalException e) {
            throw new CustomException("内部异常");
        }
    }

    @Override
    public MinioObject statObject(String bucketName, String objectName) {
        return statObject(bucketName, objectName, 60 * 60 * 24);
    }

    @Override
    public MinioObject statObject(String bucketName, String objectName, Integer expires) {
        try {
            MinioObject minioObject;
            if (bucketExists(bucketName, false)) {
                minioObject = new MinioObject(minioClient.statObject(bucketName, objectName));
                String url = minioClient.presignedGetObject(bucketName, objectName, expires);
                minioObject.setPath(url);
            } else {
                minioObject = new MinioObject();
            }
            return minioObject;
        } catch (XmlPullParserException e) {
            throw new CustomException("解析返回的XML异常");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (InvalidBucketNameException e) {
            throw new CustomException("不合法的存储桶名称: '" + bucketName + "'");
        } catch (InsufficientDataException e) {
            throw new CustomException("Minio内部异常");
        } catch (NoResponseException e) {
            throw new CustomException("服务器无响应");
        } catch (ErrorResponseException e) {
            throw new CustomException("执行失败");
        } catch (InternalException e) {
            throw new CustomException("内部异常");
        } catch (InvalidExpiresRangeException e) {
            throw new CustomException("presigned URL已经过期了");
        }
    }


    @Override
    public boolean bucketExists(String bucketName, boolean insecure) {
        try {
            // 检查'存储桶'是否存在。
            boolean exists = minioClient.bucketExists(bucketName);
            if (!exists && insecure) {
                minioClient.makeBucket(bucketName);
                return true;
            }
            return exists;
        } catch (XmlPullParserException e) {
            log.error("解析返回的XML异常{}", e.getMessage());
            throw new CustomException("解析返回的XML异常");
        } catch (InvalidBucketNameException e) {
            throw new CustomException("不合法的存储桶名称: '" + bucketName + "'");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (MinioException e) {
            log.error("bucketExists方法：{}", e.getMessage());
            throw new CustomException("Minio内部异常");
        }
    }

    @Override
    public List<Bucket> listBuckets() {
        try {
            // 列出所有存储桶
            return minioClient.listBuckets();
        } catch (XmlPullParserException e) {
            throw new CustomException("解析返回的XML异常");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (MinioException e) {
            log.error("listBuckets方法：{}", e.getMessage());
            throw new CustomException("Minio内部异常");
        }
    }

    @Override
    public void removeBucket(String bucketName, boolean insecure) {
        try {
            // 删除之前先检查`my-bucket`是否存在。
            if (bucketExists(bucketName, false)) {
                List<MinioItem> minioItems = listObjects(bucketName);
                if (minioItems.isEmpty()) {
                    // 删除存储桶，注意，只有存储桶为空时才能删除成功。
                    minioClient.removeBucket(bucketName);
                    log.info("{} is removed successfully", bucketName);
                } else {
                    if (insecure) {
                        List<String> stringList = minioItems.stream().map(MinioItem::getObjectName).collect(Collectors.toList());
                        removeObject(bucketName, stringList);
                        // 删除存储桶，注意，只有存储桶为空时才能删除成功。
                        minioClient.removeBucket(bucketName);
                        log.info("{} is removed successfully", bucketName);
                    } else {
                        log.warn("{}: Include file object", bucketName);
                    }
                }
            } else {
                log.warn("{}: does not exist", bucketName);
            }
        } catch (InvalidBucketNameException e) {
            throw new CustomException("不合法的存储桶名称: '" + bucketName + "'");
        } catch (XmlPullParserException e) {
            throw new CustomException("解析返回的XML异常");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (MinioException e) {
            log.error("removeBucket方法：{}", e.getMessage());
            throw new CustomException("Minio内部异常");
        }
    }


    @Override
    public List<MinioItem> listObjects(String bucketName) {
        try {
            List<MinioItem> items = new ArrayList<>(16);
            // 检查'bucketName'是否存在。
            if (bucketExists(bucketName, false)) {
                // 列出'bucketName'里的对象
                Iterable<Result<Item>> myObjects = minioClient.listObjects(bucketName);
                for (Result<Item> itemResult : myObjects) {
                    items.add(new MinioItem(itemResult.get()));
                }
                return items;
            } else {
                log.error("{} does not exist", bucketName);
            }
        } catch (InvalidBucketNameException e) {
            throw new CustomException("不合法的存储桶名称: '" + bucketName + "'");
        } catch (XmlPullParserException e) {
            throw new CustomException("解析返回的XML异常");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (MinioException e) {
            log.error("listObjects方法：{}", e.getMessage());
            throw new CustomException("Minio内部异常");
        }
        return null;
    }

    @Override
    public void removeObject(String bucketName, String objectName) {
        try {
            // 检查'bucketName'是否存在。
            if (bucketExists(bucketName, false)) {
                // 列出'bucketName'里的对象
                minioClient.removeObject(bucketName, objectName);
            } else {
                log.error("{} does not exist", bucketName);
            }
        } catch (InvalidBucketNameException e) {
            throw new CustomException("不合法的存储桶名称: '" + bucketName + "'");
        } catch (XmlPullParserException e) {
            throw new CustomException("解析返回的XML异常");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (MinioException e) {
            log.error("removeObject方法：{}", e.getMessage());
            throw new CustomException("Minio内部异常");
        }
    }

    @Override
    public void removeIncompleteUpload(String bucketName, String objectName) {
        try {
            // 检查'bucketName'是否存在。
            if (bucketExists(bucketName, false)) {
                // 列出'bucketName'里的对象
                minioClient.removeIncompleteUpload(bucketName, objectName);
            } else {
                log.error("{} does not exist", bucketName);
            }
        } catch (InvalidBucketNameException e) {
            throw new CustomException("不合法的存储桶名称: '" + bucketName + "'");
        } catch (XmlPullParserException e) {
            throw new CustomException("解析返回的XML异常");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (MinioException e) {
            log.error("removeObject方法：{}", e.getMessage());
            throw new CustomException("Minio内部异常");
        }
    }

    @Override
    public void removeObject(String bucketName, List<String> objectNames) {
        objectNames.forEach(objectName -> removeObject(bucketName, objectName));
    }

    @Override
    public InputStream getObject(String bucketName, String objectName) {
        try {
            // 调用statObject()来判断对象是否存在。
            // 如果不存在, statObject()抛出异常,
            // 否则则代表对象存在。
            minioClient.statObject(bucketName, objectName);

            // 获取'objectName'的流并保存到'objectName'文件中。
            return minioClient.getObject(bucketName, objectName);

        } catch (InvalidBucketNameException e) {
            throw new CustomException("不合法的存储桶名称: '" + bucketName + "'");
        } catch (XmlPullParserException e) {
            throw new CustomException("解析返回的XML异常");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("找不到相应的签名算法");
        } catch (InvalidKeyException e) {
            throw new CustomException("不合法的access key或者secret key");
        } catch (IOException e) {
            throw new CustomException("Minio连接异常");
        } catch (MinioException e) {
            log.error("getObject方法：{}", e.getMessage());
            throw new CustomException("Minio内部异常");
        }
    }

}
