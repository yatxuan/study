package com.yat.social.oauth.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.HttpUtil;
import com.yat.social.oauth.cache.AuthStateCache;
import com.yat.social.oauth.config.AuthDefaultSource;
import com.yat.social.oauth.enums.AuthUserGender;
import com.yat.social.oauth.model.AuthCallback;
import com.yat.social.oauth.model.AuthToken;
import com.yat.social.oauth.model.AuthUser;
import com.yat.social.oauth.utils.GlobalAuthUtils;
import com.yat.social.oauth.utils.UrlBuilder;
import com.yat.social.oauth.config.AuthConfig;
import com.yat.social.oauth.exception.AuthException;

/**
 * 钉钉登录
 *
 * @author Yat-Xuan
 * @since 1.0.0
 */
public class AuthDingTalkRequest extends AuthDefaultRequest {

    public AuthDingTalkRequest(AuthConfig config) {
        super(config, AuthDefaultSource.DINGTALK);
    }

    public AuthDingTalkRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.DINGTALK, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        return AuthToken.builder().accessCode(authCallback.getCode()).build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String code = authToken.getAccessCode();
        JSONObject param = new JSONObject();
        param.put("tmp_auth_code", code);
        String response = HttpUtil.post(userInfoUrl(authToken), param.toJSONString());
        JSONObject object = JSON.parseObject(response);
        if (object.getIntValue("errcode") != 0) {
            throw new AuthException(object.getString("errmsg"));
        }
        object = object.getJSONObject("user_info");
        AuthToken token = AuthToken.builder()
            .openId(object.getString("openid"))
            .unionId(object.getString("unionid"))
            .build();
        return AuthUser.builder()
            .uuid(object.getString("unionid"))
            .nickname(object.getString("nick"))
            .username(object.getString("nick"))
            .gender(AuthUserGender.UNKNOWN)
            .source(source.toString())
            .token(token)
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
            .queryParam("appid", config.getClientId())
            .queryParam("scope", "snsapi_login")
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
        // 根据timestamp, appSecret计算签名值
        String timestamp = System.currentTimeMillis() + "";
        String urlEncodeSignature = GlobalAuthUtils.generateDingTalkSignature(config.getClientSecret(), timestamp);

        return UrlBuilder.fromBaseUrl(source.userInfo())
            .queryParam("signature", urlEncodeSignature)
            .queryParam("timestamp", timestamp)
            .queryParam("accessKey", config.getClientId())
            .build();
    }
}
