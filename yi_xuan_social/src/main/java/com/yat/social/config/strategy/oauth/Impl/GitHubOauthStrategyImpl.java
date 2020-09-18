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
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.yat.social.common.constant.OauthType.OAUTH_GITHUB;

/**
 * <p>Description: GitHub 授权 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/19 - 1:52
 */
@Component
@RequiredArgsConstructor
@HandlerFileOauthType(OAUTH_GITHUB)
public class GitHubOauthStrategyImpl implements BaseOauthStrategy {

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
            redirectUri = domain + "oauth/" + OAUTH_GITHUB + "/callback";
        }

        AuthConfig authConfig = AuthConfig.builder()
                .clientId("Iv1.8bac98496cfaad52")
                .clientSecret("3f8c378aa507dbe0a0840a7a32b6e864d1fcc140")
                .redirectUri(redirectUri)
                .build();
        return new AuthGithubRequest(authConfig, authStateCache);
    }

    @Override
    public String getAuthorizedAddress(String redirectUri, String state) {
        if (StrUtil.isBlank(state)) {
            state = OAUTH_GITHUB + ":" + AuthStateUtils.createState();
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
