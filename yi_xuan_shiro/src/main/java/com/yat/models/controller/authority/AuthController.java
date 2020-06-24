package com.yat.models.controller.authority;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.wf.captcha.ArithmeticCaptcha;
import com.yat.common.constant.CommonConstant;
import com.yat.common.exception.BadRequestException;
import com.yat.common.utils.ResultResponse;
import com.yat.common.utils.StringUtils;
import com.yat.config.properties.RsaSecret;
import com.yat.config.redis.RedisUtils;
import com.yat.config.shiro.realm.ShiroRealm;
import com.yat.models.entity.UserEntity;
import com.yat.models.entity.from.LoginFrom;
import com.yat.models.service.ILoginService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.yat.common.constant.JwtConstant.ONLINE_USER_LOGIN_TIMES;

/**
 * <p>Description: 系统用户登陆接口 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/6/20 - 19:38
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class AuthController {

    private final ILoginService loginService;
    private final RsaSecret rsaSecret;
    private final RedisUtils<String> redisUtils;

    /**
     * 登陆测试
     */
    @GetMapping("/login")
    public ResultResponse login(@RequestParam(value = "squeeze", required = false, defaultValue = "-1") int squeeze,
                                HttpServletRequest request) {
        log.debug("进入登录方法-------------------------->");
        String username = "admin", password = "d82494f05d6917ba02f7aaa29689ccb444bb73f20380876cb05d1f37537b7892";
        return loginService.login(username, password, squeeze, request);
    }

    /**
     * 用户登陆
     *
     * @param loginFrom 用户信息
     * @param request   、
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
        String verificationCode = redisUtils.get(CommonConstant.CODE_KEY + loginFrom.getUuid(), 1);

        if (StringUtils.isBlank(verificationCode)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(loginFrom.getCode()) || !StringUtils.equalsIgnoreCase(verificationCode, loginFrom.getCode())) {
            throw new BadRequestException("验证码错误");
        }

        return loginService.login(username, password, loginFrom.getSqueeze(), request);

    }

    /**
     * 退出登陆
     *
     * @return 、
     */
    @DeleteMapping("/logout")
    public ResultResponse logout(HttpServletRequest request) {
        loginService.logout(request);
        return ResultResponse.success();
    }


    /**
     * 当前判断 只要满足其中任意一个权限
     * RequiresPermissions(value = {"user:info", "user"}, logical = Logical.OR)
     *
     * @return 、
     */
    @GetMapping("info")
    @RequiresPermissions(value = {"user:info", "user"}, logical = Logical.OR)
    public ResultResponse info() {
        UserEntity principal = (UserEntity) SecurityUtils.getSubject().getPrincipal();

        return ResultResponse.success(principal);
    }

    /**
     * 获取验证码
     *
     * @return /
     */
    @GetMapping(value = "/captchaImage")
    public ResultResponse getCode() {
        String rest = "0.0";
        // 算术类型 https://gitee.com/whvse/EasyCaptcha  1921682041admin
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();
        if (StringUtils.equals(rest, result)) {
            result = "0";
        }
        String uuid = IdUtil.simpleUUID();
        // 保存
        redisUtils.set(CommonConstant.CODE_KEY + uuid, result, rsaSecret.getExpiration());
        ResultResponse response = ResultResponse.success();
        response.put("uuid", uuid);
        response.put("img", captcha.toBase64());
        return response;

    }
}
