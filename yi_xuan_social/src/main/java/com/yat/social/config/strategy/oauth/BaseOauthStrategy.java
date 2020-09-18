package com.yat.social.config.strategy.oauth;

import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;

/**
 * <p>Description: 定义总策略 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/9/18 - 17:57
 */
public interface BaseOauthStrategy {

    /**
     * 第三方授权，获取用户信息
     *
     * @param redirectUri 回调地址
     * @return 、
     */
    AuthRequest getAuthRequest(String redirectUri);

    /**
     * 获取授权回调地址
     *
     * @param redirectUri 回调地址
     * @param state       授权时，携带的参数
     * @return 回调地址
     */
    String getAuthorizedAddress(String redirectUri, String state);

    /**
     * 登陆回调，获取用户信息
     *
     * @param callback 回调数据
     * @return 用户信息
     */
    AuthResponse getCallback(AuthCallback callback);

    /**
     * 取消授权回调页面
     */
    AuthResponse revoke(String token);
}
