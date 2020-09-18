package com.yat.social.config.strategy.oauth.Impl;

import cn.hutool.core.util.StrUtil;
import com.yat.social.common.annotation.HandlerFileOauthType;
import com.yat.social.config.strategy.oauth.BaseOauthStrategy;
import com.yat.social.justauth.cache.AuthStateRedisCache;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatMpRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.yat.social.common.constant.OauthType.OAUTH_WECHAT_MP;

/**
 * <p>Description: 微信公众号 授权 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/19 - 1:55
 */
@Component
@RequiredArgsConstructor
@HandlerFileOauthType(OAUTH_WECHAT_MP)
public class WeChatMpOauthStrategyImpl implements BaseOauthStrategy {

    /**
     * 回调域名
     */
    @Value("${oauth.callback.domain}")
    private String domain;

    private final AuthStateRedisCache authStateCache;

    @Override
    public AuthRequest getAuthRequest(String redirectUri) {

        if (StrUtil.isBlank(redirectUri)) {
            domain = StrUtil.appendIfMissing(domain, "/");
            redirectUri = domain + "oauth/" + OAUTH_WECHAT_MP + "/callback";
        }

        AuthConfig authConfig = AuthConfig.builder()
                .clientId("wx3a9c99cd8dab548f")
                .clientSecret("8cbd676499edb5372a545d164dd7e2ed")
                .redirectUri(redirectUri)
                .build();

        return new AuthWeChatMpRequest(authConfig, authStateCache);

    }

    @Override
    public String getAuthorizedAddress(String redirectUri, String state) {
        if (StrUtil.isBlank(state)) {
            state = OAUTH_WECHAT_MP + ":" + AuthStateUtils.createState();
        }
        // 获取授权地址
        return getAuthRequest(redirectUri).authorize(state);
    }

    @Override
    public AuthResponse getCallback(AuthCallback callback) {
        return getAuthRequest(null).login(callback);
    }

    @Override
    public AuthResponse revoke(String token) {
        AuthToken authToken = AuthToken.builder().accessToken(token).build();
        return getAuthRequest(null).revoke(authToken);
    }
}
