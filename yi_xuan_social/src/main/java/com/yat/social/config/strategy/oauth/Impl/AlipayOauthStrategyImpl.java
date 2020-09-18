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
import me.zhyd.oauth.request.AuthAlipayRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.yat.social.common.constant.OauthType.OAUTH_ALIPAY;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/19 - 1:57
 */
@Component
@RequiredArgsConstructor
@HandlerFileOauthType(OAUTH_ALIPAY)
public class AlipayOauthStrategyImpl implements BaseOauthStrategy {

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
            redirectUri = domain + "oauth/" + OAUTH_ALIPAY + "/callback";
        }

        AuthConfig authConfig = AuthConfig.builder()
                .clientId("2018120962441761")
                .clientSecret("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCW6qdZakwYZY3ldjiQmYqYcJxuE1I1oG1GtxJCtfq8+H0qiylQmifAsFbCzKNMwzJMmOoo73BQiwg2F6sW+aPlIKqffpzrgnEgpxVl7TRsx3R6nkkAly0xPhAFkX7sNf2HqEu1p64E/ooSKU4CCweTusL817vqNyitmT1qyP70IzOpNnbGGJQo83N7iG08zM+Rcf7azh86p0tDIjFXHOffL+xDxK88ZShxtMn0Vu8IdKmvnz7txbS+LBVXXI72jbaGJ90uoKwfHbzm8/7zc0/K/9aPdvz2qHXXvgVHO9y5e6OpsqxQnwy1nJQMqMQFQZEkAVKSltgNtP6eE4tMKoQBAgMBAAECggEBAJN5VnNwBlBI3PKAJiJXiDISySpfwxQYYvCZcUSzZARJprZuKr9TBMmtB/tv0ZvvmzVysE+0O0LZufpyAoR/CoOa+mDFbz5Mx5Oq8qQZugManV49UwU6tbTK+wkV9ruIvulTsjgfVVQYEIBS0KH6eTsJZAWEI2Nq51tMxdGV/F5Aj8SoP8gZhsxzQk8OS/k09WsgfmR8qiuZILf+CiDqb/Hc90C8aJY7m3Hv5xBbFmcIEhF739A6pbyAu0XivE0uAbZf6zYr+gl/hkZ/yr05sG3BtTcZgDj9/nu6uU/HTSJ7o7oC7bILjlRqMDsT4Bo9Jo9QoN5X5/Ll7s7Kn9mTZYECgYEAx9eRSpeUIjPe1tCpZnVmlHPTL2E+lVDE6laqLThnuoGSiltRycGrmPlOLkpI0zdyqhydlBv6z6UHD0h8Xx0wIIRusc+ht0k8QhEYRurJIdLswpQPZbP1h5ndvoe4pWSJ7zgh3gqdrepQRw+IeRPvLG+dwE8T3692Z06oR3gISFkCgYEAwVNwoRqEauXn/4uZ7oRNu4TjkTc9cB8ZpSNyGXhZwwKhfstTRZkGMQGRxIpY6ZvherH6eYwj8tg3pPOMtHdliYR0eusjoq742XH4uAmrnrikwzdAIaffJtZxcODhVbBSM2CxsHkeaeo6STXO+zwJKo6TtwsvetTQriwdzykQo+kCgYBgPS8KFg9PnV0+ZESARB9/dHwKgDFMZRY/qDbSCiziA45XtPyhRfRmegEth7akiTpeM+OrNQJHRzksIgNLJtpLfpt0ZAKEKu+q/WKMnHr6IALG/digaNbgU7gXDnNcqxtGKekdbk1bONEqN4/Ih1ISOzpkJ3DxsyU5QQcHTAXpGQKBgCTSAYeNh17cvtz696xn4m4ulzEXNkpeggHjyF11gJMVszB1BT0iMf2vcNXgC6Min0VBFJWFzWr5J0/xWwEcJGiWo6CtnDHl5Vt0y4wxu1AVlrvfkAVY9bVt7lkzW3d4h49lgTI9Pz+o7sT2zJ8pBg3m7ddU+AwK8Fvb5ymHhrXRAoGAPBxDmxsHnu0s/9+J5q+1b3d70+GlIEHXa98xV5+XnRwOoiUVs6LwxeCi6/pab18lpcf0F98hq4/gwje1uQmHdmEiIqWJaP6SKP3xvYaY0Z8lxGks67+Kl/rQc3uOye3WKbV1Vcc1bwL0nH8yBNpJwujpnYF2JVjB/vOTqSM3iDA=")
                .alipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmvllsqOotuPFOL63gKprWig3UcGGsyn4AIY5jU8P+/WWjaf/fkKtQNRImvg0cRBmdLOMPFrlLB6m5GYG2wUqezQ0pirIh68Pe6L5/Y8LyWFTEsXGSEH0MqYVjy/Q78+DlPwSwlskmUULqw8mHWYEN6PKDjqCBtdYbFNxUJ+GNu78lmb3oXtSIvxUiL1GQ2WezdShXZSDKZaImIr3R0/mqlzk29BL9mcX0u9e06NziC9nDgxc35wuLe3xlU8B84PWSa/WUtk6gojqQQPjiHwErrFn6bKdY3uiPU8dU5XKsPYBPKGbcRvgDZfH4As7spQvpYKNp9kJ1RLVPok8dIsnZQIDAQAB")
                .redirectUri(redirectUri)
                .build();

        return new AuthAlipayRequest(authConfig, authStateCache);
    }

    @Override
    public String getAuthorizedAddress(String redirectUri, String state) {
        if (StrUtil.isBlank(state)) {
            state = OAUTH_ALIPAY + ":" + AuthStateUtils.createState();
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
