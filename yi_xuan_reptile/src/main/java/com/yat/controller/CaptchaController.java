package com.yat.controller;

import com.yat.common.util.ResultResponse;
import com.yat.common.util.ip.AddressUtils;
import com.yat.reptile.ValidateFormService;
import com.yat.reptile.module.VerifyImageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/22 - 13:58
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/captcha")
public class CaptchaController {

    private final ValidateFormService validateFormService;

    /**
     * 验证：验证码
     *
     * @return 、
     */
    @PostMapping("/validate")
    public ResultResponse validate(Map<String, String> params, HttpServletRequest request) {
        return validateFormService.validate(params, request) ?
                ResultResponse.success() :
                ResultResponse.error("验证码错误", refreshCode(params.get("verifyId")));
    }


    /**
     * 刷新验证码
     *
     * @return 、
     */
    @GetMapping("/refresh/{verifyId}")
    public ResultResponse refresh(@PathVariable("verifyId") String verifyId) {
        return ResultResponse.success("操作成功", refreshCode(verifyId));
    }

    /**
     * 刷新验证码
     *
     * @param verifyId 验证码的UUID
     * @return 、
     */
    private Map refreshCode(String verifyId) {
        VerifyImageVO verifyImageVO = validateFormService.refresh(verifyId);
        Map<String, String> map = new HashMap<>(2);
        map.put("verifyId", verifyImageVO.getVerifyId());
        map.put("verifyImgStr", verifyImageVO.getVerifyImgStr());
        return map;
    }


    @GetMapping("/set")
    public void setList(HttpServletRequest request) {

        String ip = AddressUtils.getIpAddr(request);
        // 获取浏览器信息
        String browser = AddressUtils.getBrowser(request);
        // 获取详细地址
        String cityInfo = AddressUtils.getCityInfo(ip);
        log.warn("当前ip'{}',浏览器为：'{}',消息地址为：'{}'", ip, browser, cityInfo);
    }
}
