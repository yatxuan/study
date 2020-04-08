package com.yat.social.oauth.request;

import com.alibaba.fastjson.JSONObject;
import com.yat.social.oauth.cache.AuthStateCache;
import com.yat.social.oauth.config.AuthDefaultSource;
import com.yat.social.oauth.enums.AuthUserGender;
import com.yat.social.oauth.model.AuthCallback;
import com.yat.social.oauth.model.AuthToken;
import com.yat.social.oauth.model.AuthUser;
import com.yat.social.oauth.config.AuthConfig;
import com.yat.social.oauth.exception.AuthException;

/**
 * CSDN登录
 *
 * @author Yat-Xuan
 * @since 1.0.0
 */
@Deprecated
public class AuthCsdnRequest extends AuthDefaultRequest {

    public AuthCsdnRequest(AuthConfig config) {
        super(config, AuthDefaultSource.CSDN);
    }

    public AuthCsdnRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.CSDN, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        this.checkResponse(accessTokenObject);
        return AuthToken.builder().accessToken(accessTokenObject.getString("access_token")).build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String response = doGetUserInfo(authToken);
        JSONObject object = JSONObject.parseObject(response);
        this.checkResponse(object);
        return AuthUser.builder()
            .uuid(object.getString("username"))
            .username(object.getString("username"))
            .remark(object.getString("description"))
            .blog(object.getString("website"))
            .gender(AuthUserGender.UNKNOWN)
            .token(authToken)
            .source(source.toString())
            .build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.containsKey("error_code")) {
            throw new AuthException(object.getString("error"));
        }
    }
}
