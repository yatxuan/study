package com.yat.minio.service;

import com.yat.minio.model.MinioItem;
import com.yat.minio.model.MinioObject;
import io.minio.messages.Bucket;
import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;
import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/14
 * @Time: 11:40
 */
public interface MinioService {

    /**
     * 上传文件
     *
     * @param bucketName  存储桶名称
     * @param objectName  存储桶里的对象名称（文件名称）
     * @param stream      要上传的流
     * @param size        要上传的stream的size
     * @param contentType 文件类型
     * @return /
     * IOException                  连接异常。
     * InvalidKeyException          不合法的access key或者secret key
     * NoSuchAlgorithmException     找不到相应的签名算法
     * InsufficientDataException    在读到相应length之前就得到一个EOFException
     * InvalidArgumentException
     * InternalException            内部错误。
     * NoResponseException          服务器无响应
     * InvalidBucketNameException   不合法的存储桶名称
     * XmlPullParserException       解析返回的XML异常。
     * ErrorResponseException       执行失败异常
     * InvalidExpiresRangeException Presigned URL已经过期了。
     */
    MinioObject saveObject(String bucketName, String objectName, InputStream stream, long size, String contentType);

    /**
     * 获取对象的元数据（自动设置过期时间，过期时间为一天）
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return /
     * XmlPullParserException       解析返回的XML异常。
     * NoSuchAlgorithmException     找不到相应的签名算法
     * InvalidKeyException          不合法的access key或者secret key
     * IOException                  连接异常。
     * InsufficientDataException    在读到相应length之前就得到一个EOFException
     * ErrorResponseException       执行失败异常
     * InvalidBucketNameException   不合法的存储桶名称
     * InternalException            内部错误
     * NoResponseException          服务器无响应
     * InvalidExpiresRangeException Presigned URL已经过期了。
     */
    MinioObject statObject(String bucketName, String objectName);


    /**
     * 获取对象的元数据（手动设置过期时间）
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    过期时间（秒）
     * @return /
     */
    MinioObject statObject(String bucketName, String objectName, Integer expires);

    /**
     * 检查存储桶是否存在,如果不存在，是否需要创建
     *
     * @param bucketName 存储桶名称
     * @param insecure   true-创建 ，false-不创建
     * @return 、
     * NoSuchAlgorithmException 找不到相应的签名算法
     * InvalidKeyException      不合法的access key或者secret key
     * IOException              连接异常。
     * XmlPullParserException   解析返回的XML异常。
     */
    boolean bucketExists(String bucketName, boolean insecure);

    /**
     * 列出所有存储桶
     *
     * @return 、
     * NoSuchAlgorithmException 找不到相应的签名算法
     * InvalidKeyException      不合法的access key或者secret key
     * IOException              连接异常。
     * XmlPullParserException   解析返回的XML异常。
     */
    List<Bucket> listBuckets();


    /**
     * 删除一个存储桶，如果该存储桶里有对象，默认不会直接删除存储桶
     *
     * @param bucketName 存储桶
     * @param insecure   true-强制删除 ，false-不删除
     *                   NoSuchAlgorithmException 找不到相应的签名算法
     *                   InvalidKeyException      不合法的access key或者secret key
     *                   IOException              连接异常。
     *                   XmlPullParserException   解析返回的XML异常。
     */
    void removeBucket(String bucketName, boolean insecure);

    /**
     * 列出某个存储桶中的所有对象。
     *
     * @param bucketName 存储桶
     * @return 、
     * XmlPullParserException   解析返回的XML异常。
     * NoSuchAlgorithmException 找不到相应的签名算法
     * InvalidKeyException      不合法的access key或者secret key
     * IOException              连接异常。
     */
    List<MinioItem> listObjects(String bucketName);

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     *                   IOException                连接异常。
     *                   InvalidKeyException        不合法的access key或者secret key
     *                   NoSuchAlgorithmException   找不到相应的签名算法
     *                   InsufficientDataException  在读到相应length之前就得到一个EOFException
     *                   InternalException          内部错误。
     *                   NoResponseException        服务器无响应
     *                   InvalidBucketNameException 不合法的存储桶名称
     *                   XmlPullParserException     解析返回的XML异常。
     *                   ErrorResponseException     执行失败异常
     */
    @Async
    void removeObject(String bucketName, String objectName);

    /**
     * 删除一个未完整上传的对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     */
    @Async
    void removeIncompleteUpload(String bucketName, String objectName);

    /**
     * 删除多个对象
     *
     * @param bucketName  存储桶名称
     * @param objectNames 含有要删除的多个object名称的迭代器对象。
     */
    @Async
    void removeObject(String bucketName, List<String> objectNames);

    /**
     * 下载并将文件保存到本地
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return InputStream 下载流
     */
    InputStream getObject(String bucketName, String objectName);
}
