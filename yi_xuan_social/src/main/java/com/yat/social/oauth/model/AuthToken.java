package com.yat.social.oauth.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 授权所需的token
 *
 * @author Yat-Xuan
 * @since 1.8
 */
@Data
@Builder
public class AuthToken implements Serializable {
    private String accessToken;
    private int expireIn;
    private String refreshToken;
    private String uid;
    private String openId;
    private String accessCode;
    private String unionId;

    /**
     * Google附带属性
     */
    private String scope;
    private String tokenType;
    private String idToken;

    /**
     * 小米附带属性
     */
    private String macAlgorithm;
    private String macKey;

    /**
     * 企业微信附带属性
     *
     * @since 1.10.0
     */
    private String code;

    /**
     * Twitter附带属性
     *
     * @since 1.13.0
     */
    private String oauthToken;
    private String oauthTokenSecret;
    private String userId;
    private String screenName;
    private Boolean oauthCallbackConfirmed;

}
