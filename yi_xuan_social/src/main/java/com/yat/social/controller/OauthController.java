package com.yat.social.controller;

import cn.hutool.json.JSONUtil;
import com.yat.social.config.strategy.oauth.HandlerOauthContext;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yat.social.common.constant.OauthType.*;

/**
 * <p>Description: 第三方登录 controller </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/8 17:47
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
public class OauthController {

    /**
     * 回调域名
     */
    @Value("${oauth.callback.domain}")
    private String domain;

    private final HandlerOauthContext handlerOauthContext;

    private final Map<String, String> map = new HashMap<>(6);

    public OauthController(HandlerOauthContext handlerOauthContext) {
        this.handlerOauthContext = handlerOauthContext;
        map.put(OAUTH_GITEE, "码云");
        map.put(OAUTH_WEIBO, "微博");
        map.put(OAUTH_GITHUB, "GitHub");
        map.put(OAUTH_WECHAT_MP, "微信公众号");
        map.put(OAUTH_QQ, "QQ");
        map.put(OAUTH_ALIPAY, "支付宝");

    }

    /**
     * 登录类型
     */
    @GetMapping
    public Map<String, String> loginType() {
        Set<String> oauthList = map.keySet();
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
     */
    @RequestMapping("/login/{oauthType}")
    public AuthResponse renderAuth(@PathVariable String oauthType,
                                   @RequestParam(value = "state", required = false) String state,
                                   @RequestParam(value = "redirectUri", required = false) String redirectUri) {
        log.info("进入授权登陆接口------------------------------>");
        // 获取授权地址
        String authorize = handlerOauthContext.getOrderStrategy(oauthType).getAuthorizedAddress(redirectUri, state);
        return AuthResponse.builder().data(authorize).build();
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
        log.info("进入授权回调接口------------------------------>");
        AuthResponse response =
                handlerOauthContext.getOrderStrategy(oauthType).getCallback(callback);
        log.info("【response】= {}", JSONUtil.toJsonStr(response));
        return response;
    }

    /**
     * 取消授权回调页面
     *
     * @param token 、
     * @return 、
     */
    @RequestMapping("/{oauthType}/revoke/{token}")
    public Object revokeAuth(@PathVariable String oauthType, @PathVariable("token") String token) {
        return handlerOauthContext.getOrderStrategy(oauthType).revoke(token);
    }

}
