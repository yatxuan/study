package com.yat.modules.authority.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.wf.captcha.ArithmeticCaptcha;
import com.yat.common.constant.CommonConstant;
import com.yat.common.constant.RedisConstant;
import com.yat.common.exception.BadRequestException;
import com.yat.common.utils.RedisUtils;
import com.yat.common.utils.ResultResponse;
import com.yat.common.utils.StringUtils;
import com.yat.config.properties.RsaSecret;
import com.yat.modules.authority.dto.LoginUser;
import com.yat.modules.authority.service.ILoginService;
import com.yat.modules.authority.service.ShiroService;
import com.yat.modules.from.LoginFrom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: 系统：系统授权接口 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/24
 * @Time: 14:28
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class AuthController {

    private final RedisUtils<String> redisUtils;
    private final RsaSecret rsaSecret;
    private final ShiroService shiroService;
    private final ILoginService loginService;

    /**
     * 登录
     * @param loginFrom 、
     * @param request 、
     * @return 、
     */
    @PostMapping("/login")
    public ResultResponse login(@RequestBody LoginFrom loginFrom,
                                HttpServletRequest request) {

        String username = loginFrom.getUsername();
        String password = loginFrom.getPassword();

        // 密码解密
        RSA rsa = new RSA(rsaSecret.getPrivateKey(), null);
        username = new String(rsa.decrypt(username, KeyType.PrivateKey));
        password = new String(rsa.decrypt(password, KeyType.PrivateKey));

        // 查询验证码,并清除验证码
        String verificationCode = redisUtils.get(RedisConstant.CODE_KEY + loginFrom.getUuid(), 1);

        if (StringUtils.isBlank(verificationCode)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(loginFrom.getCode()) || !StringUtils.equalsIgnoreCase(verificationCode, loginFrom.getCode())) {
            throw new BadRequestException("验证码错误");
        }

        return loginService.login(username, password, request);

    }

    /**
     * 获取验证码
     *
     * @return /
     */
    @GetMapping(value = "/captchaImage")
    public ResultResponse getCode() {
        String rest = "0.0";
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();
        if (StringUtils.equals(rest, result)) {
            result = CommonConstant.NUM_ZERO;
        }
        String uuid = IdUtil.simpleUUID();
        // 保存
        redisUtils.set(RedisConstant.CODE_KEY + uuid, result, rsaSecret.getExpiration());
        ResultResponse response = ResultResponse.success();
        response.put("uuid", uuid);
        response.put("img", captcha.toBase64());
        return response;

    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public ResultResponse getInfo(HttpServletRequest request) {
        LoginUser currUser = shiroService.getCurrUser(request);
        ResultResponse response = ResultResponse.success();
        response.put("user", currUser);
        return response;
    }

    /**
     * 退出登陆
     *
     * @param request 、
     * @return 、
     */
    @DeleteMapping(value = "/logout")
    public ResultResponse logout(HttpServletRequest request) {
        return ResultResponse.success();
    }


}
