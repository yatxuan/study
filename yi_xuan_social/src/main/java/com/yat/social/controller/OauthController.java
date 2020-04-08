package com.yat.social.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xkcoding.justauth.AuthRequestFactory;
import me.zhyd.oauth.model.AuthCallback;
import com.yat.social.oauth.utils.AuthStateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: 第三方登录 Controller </p>
 * @author Yat-Xuan
 * @date 2020/4/8 17:47
*/
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthController {
    private final AuthRequestFactory factory;

    static Map<String, String> map = new HashMap<>(8);

    static {
        map.put("microsoft", "微软");
        map.put("google", "谷歌");
        map.put("github", "GitHub");
        map.put("mi", "小米");
        map.put("wechat", "微信");
        map.put("wechat_enterprise", "企业微信");
        map.put("qq", "QQ");
        map.put("alipay", "支付宝");
    }


    /**
     * 登录类型
     */
    @GetMapping
    public Map<String, String> loginType() {
        List<String> oauthList = factory.oauthList();
        return oauthList.stream().collect(
                Collectors.toMap(
                        oauth -> map.get(oauth.toLowerCase()) + "登录", oauth -> "http://oauth.yatxuan.cn/oauth/login/" + oauth.toLowerCase()
                )
        );
    }

    /**
     * 登录
     *
     * @param oauthType 第三方登录类型
     * @param response  response
     * @throws IOException
     */
    @RequestMapping("/login/{oauthType}")
    public void renderAuth(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        response.sendRedirect(authRequest.authorize(oauthType + ":" + AuthStateUtils.createState()));
    }

    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     * @return 登录成功后的信息
     */
    @RequestMapping("/{oauthType}/callback")
    public AuthResponse login(@PathVariable String oauthType, AuthCallback callback) {
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        AuthResponse response = authRequest.login(callback);
        log.info("【response】= {}", JSONUtil.toJsonStr(response));
        return response;
    }

    private AuthSource getAuthSource(String type) {
        if (StrUtil.isNotBlank(type)) {
            return AuthSource.valueOf(type.toUpperCase());
        } else {
            throw new RuntimeException("不支持的类型");
        }
    }
}
