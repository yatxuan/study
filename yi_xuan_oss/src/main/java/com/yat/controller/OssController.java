package com.yat.controller;

import com.yat.common.ResultResponse;
import com.yat.component.OssCallbackResult;
import com.yat.component.OssPolicyResult;
import com.yat.service.IOssService;
import com.yat.service.ISimpleOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/18 - 10:14
 */
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OssController {

    private final ISimpleOssService simpleOssService;

    /**
     * 简单文件上传测试
     * @return 、
     */
    @GetMapping
    public String testUpload(){
        return simpleOssService.localFilesUpload();
    }

    /**
     * oss上传签名生成
     */
    @GetMapping("/policy")
    public ResultResponse policy() {
        // OssPolicyResult ossPolicyResult = ossService.policy();
        return ResultResponse.success();
    }

    /**
     * oss上传成功回调
     */
    @PostMapping("callback")
    public ResultResponse callback(HttpServletRequest request) {
        // OssCallbackResult ossCallbackResult = ossService.callback(request);
        return ResultResponse.success();
    }
}
