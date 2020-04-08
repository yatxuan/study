package com.yat.social.oauth.request;

import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.HttpUtil;
import com.xkcoding.http.support.HttpHeader;
import com.yat.social.oauth.cache.AuthStateCache;
import com.yat.social.oauth.config.AuthConfig;
import com.yat.social.oauth.config.AuthDefaultSource;
import com.yat.social.oauth.enums.AuthUserGender;
import com.yat.social.oauth.exception.AuthException;
import com.yat.social.oauth.model.AuthCallback;
import com.yat.social.oauth.model.AuthToken;
import com.yat.social.oauth.model.AuthUser;
import com.yat.social.oauth.utils.UrlBuilder;

/**
 * Google登录
 *
 * @author Yat-Xuan
 * @since 1.3.0
 */
public class AuthGoogleRequest extends AuthDefaultRequest {

    public AuthGoogleRequest(AuthConfig config) {
        super(config, AuthDefaultSource.GOOGLE);
    }

    public AuthGoogleRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.GOOGLE, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        this.checkResponse(accessTokenObject);
        return AuthToken.builder()
            .accessToken(accessTokenObject.getString("access_token"))
            .expireIn(accessTokenObject.getIntValue("expires_in"))
            .scope(accessTokenObject.getString("scope"))
            .tokenType(accessTokenObject.getString("token_type"))
            .idToken(accessTokenObject.getString("id_token"))
            .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Authorization", "Bearer " + authToken.getAccessToken());
        String userInfo = HttpUtil.post(userInfoUrl(authToken), null, httpHeader);
        JSONObject object = JSONObject.parseObject(userInfo);
        this.checkResponse(object);
        return AuthUser.builder()
            .uuid(object.getString("sub"))
            .username(object.getString("email"))
            .avatar(object.getString("picture"))
            .nickname(object.getString("name"))
            .location(object.getString("locale"))
            .email(object.getString("email"))
            .gender(AuthUserGender.UNKNOWN)
            .token(authToken)
            .source(source.toString())
            .build();
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     * @since 1.9.3
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(source.authorize())
            .queryParam("response_type", "code")
            .queryParam("client_id", config.getClientId())
            .queryParam("scope", "openid%20email%20profile")
            .queryParam("redirect_uri", config.getRedirectUri())
            .queryParam("state", getRealState(state))
            .build();
    }

    /**
     * 返回获取userInfo的url
     *
     * @param authToken 用户授权后的token
     * @return 返回获取userInfo的url
     */
    @Override
    protected String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo()).queryParam("access_token", authToken.getAccessToken()).build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.containsKey("error") || object.containsKey("error_description")) {
            throw new AuthException(object.containsKey("error") + ":" + object.getString("error_description"));
        }
    }
}
