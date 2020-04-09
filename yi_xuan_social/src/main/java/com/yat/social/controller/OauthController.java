package com.yat.social.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import me.zhyd.oauth.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: 第三方登录 Controller </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/8 17:47
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthController {

    // private final AuthStateCache authStateCache;

    static Map<String, String> map = new HashMap<>(8);

    static {
        map.put("gitee", "码云");
        map.put("google", "谷歌");
        map.put("github", "GitHub");
        map.put("mi", "小米");
        map.put("wechat", "微信");
        map.put("qq", "QQ");
        map.put("alipay", "支付宝");
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
     * @param response  response
     * @throws IOException 、
     */
    @RequestMapping("/login/{oauthType}")
    public void renderAuth(@PathVariable String oauthType, String redirectUri, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest(oauthType, redirectUri);
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
        AuthRequest authRequest = getAuthRequest(oauthType, null);
        AuthResponse response = authRequest.login(callback);
        log.info("【response】= {}", JSONUtil.toJsonStr(response));
        return response;
    }

    /**
     * 取消授权回调页面
     *
     * @param token /
     * @return /
     */
    @RequestMapping("/{oauthType}/revoke/{token}")
    public Object revokeAuth(@PathVariable String oauthType, @PathVariable("token") String token)   {
        AuthRequest authRequest = getAuthRequest(oauthType, null);
        return authRequest.revoke(AuthToken.builder().accessToken(token).build());
    }

    private AuthDefaultSource getAuthSource(String type) {
        if (StrUtil.isNotBlank(type)) {
            return AuthDefaultSource.valueOf(type.toUpperCase());
        } else {
            throw new RuntimeException("不支持的类型");
        }
    }


    private AuthRequest getAuthRequest(String oauthType, String redirectUri) {

        if (StrUtil.isBlank(oauthType)) {
            throw new RuntimeException("不支持的类型");
        }

        if (StringUtils.isEmpty(redirectUri)) {
            redirectUri = "http://oauth.yatxuan.cn/oauth/" + oauthType + "/callback";
        }
        switch (oauthType) {
            case "gitee":
                return new AuthGiteeRequest(AuthConfig.builder()
                        .clientId("bff355206d492b8748347e52a310b2b07e48a20cc6bd585bfea62b5f45004d61")
                        .clientSecret("df1bc45c68b135396a902facdd98da605bfb1fc3fa25f89ea87f7a581af23f67")
                        .redirectUri(redirectUri)
                        .build());
            case "github":
                return new AuthGithubRequest(AuthConfig.builder()
                        .clientId("Iv1.8bac98496cfaad52")
                        .clientSecret("3f8c378aa507dbe0a0840a7a32b6e864d1fcc140")
                        .redirectUri(redirectUri)
                        .build());
            case "alipay":
                return new AuthAlipayRequest(AuthConfig.builder()
                        .clientId("2021001153644336")
                        .clientSecret("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCW6qdZakwYZY3ldjiQmYqYcJxuE1I1oG1GtxJCtfq8+H0qiylQmifAsFbCzKNMwzJMmOoo73BQiwg2F6sW+aPlIKqffpzrgnEgpxVl7TRsx3R6nkkAly0xPhAFkX7sNf2HqEu1p64E/ooSKU4CCweTusL817vqNyitmT1qyP70IzOpNnbGGJQo83N7iG08zM+Rcf7azh86p0tDIjFXHOffL+xDxK88ZShxtMn0Vu8IdKmvnz7txbS+LBVXXI72jbaGJ90uoKwfHbzm8/7zc0/K/9aPdvz2qHXXvgVHO9y5e6OpsqxQnwy1nJQMqMQFQZEkAVKSltgNtP6eE4tMKoQBAgMBAAECggEBAJN5VnNwBlBI3PKAJiJXiDISySpfwxQYYvCZcUSzZARJprZuKr9TBMmtB/tv0ZvvmzVysE+0O0LZufpyAoR/CoOa+mDFbz5Mx5Oq8qQZugManV49UwU6tbTK+wkV9ruIvulTsjgfVVQYEIBS0KH6eTsJZAWEI2Nq51tMxdGV/F5Aj8SoP8gZhsxzQk8OS/k09WsgfmR8qiuZILf+CiDqb/Hc90C8aJY7m3Hv5xBbFmcIEhF739A6pbyAu0XivE0uAbZf6zYr+gl/hkZ/yr05sG3BtTcZgDj9/nu6uU/HTSJ7o7oC7bILjlRqMDsT4Bo9Jo9QoN5X5/Ll7s7Kn9mTZYECgYEAx9eRSpeUIjPe1tCpZnVmlHPTL2E+lVDE6laqLThnuoGSiltRycGrmPlOLkpI0zdyqhydlBv6z6UHD0h8Xx0wIIRusc+ht0k8QhEYRurJIdLswpQPZbP1h5ndvoe4pWSJ7zgh3gqdrepQRw+IeRPvLG+dwE8T3692Z06oR3gISFkCgYEAwVNwoRqEauXn/4uZ7oRNu4TjkTc9cB8ZpSNyGXhZwwKhfstTRZkGMQGRxIpY6ZvherH6eYwj8tg3pPOMtHdliYR0eusjoq742XH4uAmrnrikwzdAIaffJtZxcODhVbBSM2CxsHkeaeo6STXO+zwJKo6TtwsvetTQriwdzykQo+kCgYBgPS8KFg9PnV0+ZESARB9/dHwKgDFMZRY/qDbSCiziA45XtPyhRfRmegEth7akiTpeM+OrNQJHRzksIgNLJtpLfpt0ZAKEKu+q/WKMnHr6IALG/digaNbgU7gXDnNcqxtGKekdbk1bONEqN4/Ih1ISOzpkJ3DxsyU5QQcHTAXpGQKBgCTSAYeNh17cvtz696xn4m4ulzEXNkpeggHjyF11gJMVszB1BT0iMf2vcNXgC6Min0VBFJWFzWr5J0/xWwEcJGiWo6CtnDHl5Vt0y4wxu1AVlrvfkAVY9bVt7lkzW3d4h49lgTI9Pz+o7sT2zJ8pBg3m7ddU+AwK8Fvb5ymHhrXRAoGAPBxDmxsHnu0s/9+J5q+1b3d70+GlIEHXa98xV5+XnRwOoiUVs6LwxeCi6/pab18lpcf0F98hq4/gwje1uQmHdmEiIqWJaP6SKP3xvYaY0Z8lxGks67+Kl/rQc3uOye3WKbV1Vcc1bwL0nH8yBNpJwujpnYF2JVjB/vOTqSM3iDA=")
                        .alipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmvllsqOotuPFOL63gKprWig3UcGGsyn4AIY5jU8P+/WWjaf/fkKtQNRImvg0cRBmdLOMPFrlLB6m5GYG2wUqezQ0pirIh68Pe6L5/Y8LyWFTEsXGSEH0MqYVjy/Q78+DlPwSwlskmUULqw8mHWYEN6PKDjqCBtdYbFNxUJ+GNu78lmb3oXtSIvxUiL1GQ2WezdShXZSDKZaImIr3R0/mqlzk29BL9mcX0u9e06NziC9nDgxc35wuLe3xlU8B84PWSa/WUtk6gojqQQPjiHwErrFn6bKdY3uiPU8dU5XKsPYBPKGbcRvgDZfH4As7spQvpYKNp9kJ1RLVPok8dIsnZQIDAQAB")
                        .redirectUri(redirectUri)
                        .build());
            case "qq":
                return new AuthQqRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(redirectUri)
                        .build());
            case "weibo":
                return new AuthWeiboRequest(AuthConfig.builder()
                        .clientId("484536050")
                        .clientSecret("b30bea033e2a345400c332995e8f547d")
                        .redirectUri(redirectUri)
                        .build());
           case "douyin":
                return new AuthDouyinRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri(redirectUri)
                        .build());
            default:
                throw new AuthException(AuthResponseStatus.UNSUPPORTED);

        }
    }
}
