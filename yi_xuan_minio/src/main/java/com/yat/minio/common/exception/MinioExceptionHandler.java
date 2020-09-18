package com.yat.minio.common.exception;

import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Description: 异常处理类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/14 11:12
 */
@Slf4j
@RestControllerAdvice
public class MinioExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {MultipartException.class})
    public ResponseEntity<ApiError> maxUploadSizeExceededError() {
        log.error("文件大小超出限制");
        return buildResponseEntity(ApiError.error("文件大小超出限制"));
    }

    @ExceptionHandler(value = {InvalidPortException.class})
    public ResponseEntity<ApiError> invalidPortError(InvalidPortException ex) {
        log.error(ThrowableUtil.getStackTrace(ex));
        return buildResponseEntity(ApiError.error(ex.getMessage()));
    }

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ApiError> handleCustomError(CustomException ex) {
        log.error(ThrowableUtil.getStackTrace(ex));
        return buildResponseEntity(ApiError.error(ex.getMessage()));
    }

    @ExceptionHandler(value = {InvalidBucketNameException.class})
    public ResponseEntity<ApiError> handleBucketNameError(InvalidBucketNameException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiError.error("不合法的存储桶名称: " + e.toString()));
    }

    @ExceptionHandler(value = {NoSuchAlgorithmException.class})
    public ResponseEntity<ApiError> handleNoSuchAlgorithmError(NoSuchAlgorithmException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        log.error("找不到相应的签名算法: {}", e.getMessage());
        return buildResponseEntity(ApiError.error("找不到相应的签名算法"));
    }

    @ExceptionHandler(value = {InsufficientDataException.class})
    public ResponseEntity<ApiError> handleInsufficientDataError(InsufficientDataException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        log.error("在读到相应length之前就得到一个EOFException: {}", e.getMessage());
        return buildResponseEntity(ApiError.error("在读到相应length之前就得到一个EOFException"));
    }

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<ApiError> handIoError(IOException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        log.error("Minio链接异常: {}", e.getMessage());
        return buildResponseEntity(ApiError.error("Minio链接异常"));
    }

    @ExceptionHandler(value = {InvalidKeyException.class})
    public ResponseEntity<ApiError> handInvalidKeyError(InvalidKeyException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        log.error("不合法的access key或者secret key: {}", e.getMessage());
        return buildResponseEntity(ApiError.error("不合法的access key或者secret key"));
    }

    @ExceptionHandler(value = {NoResponseException.class})
    public ResponseEntity<ApiError> handNoResponseError(NoResponseException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        log.error("服务器无响应: {}", e.getMessage());
        return buildResponseEntity(ApiError.error("服务器无响应"));
    }

    @ExceptionHandler(value = {XmlPullParserException.class})
    public ResponseEntity<ApiError> handXmlPullParserError(XmlPullParserException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        log.error("解析返回的XML异常: {}", e.getMessage());
        return buildResponseEntity(ApiError.error("解析返回的XML异常"));
    }

    @ExceptionHandler(value = {ErrorResponseException.class})
    public ResponseEntity<ApiError> handErrorResponseError(ErrorResponseException e) {
        log.error(ThrowableUtil.getStackTrace(e));
        log.error("执行失败: {}", e.getMessage());
        return buildResponseEntity(ApiError.error("执行失败"));
    }

    @ExceptionHandler(value = {InternalException.class})
    public ResponseEntity<ApiError> handInternalError(InternalException e) {
        log.error("内部异常: {}", e.getMessage());
        return buildResponseEntity(ApiError.error("内部异常"));
    }


    /**
     * 统一返回
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
