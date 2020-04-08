package com.yat.social.entity;

import lombok.Data;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/8
 * @Time: 15:43
 */
@Data
public class ResponseGitHubEntity {

    /**
     * {
     *     "data": {
     *         "uuid": "42872304",
     *         "username": "yatxuan",
     *         "nickname": null,
     *         "avatar": "https://avatars3.githubusercontent.com/u/42872304?v=4",
     *         "blog": "",
     *         "company": null,
     *         "location": null,
     *         "email": null,
     *         "remark": null,
     *         "gender": "UNKNOWN",
     *         "source": "GITHUB",
     *         "token": {
     *             "accessToken": "509b478bc6f2d10513d103b3d1b85d90b4d66661",
     *             "expireIn": 0,
     *             "refreshToken": null,
     *             "uid": null,
     *             "openId": null,
     *             "accessCode": null,
     *             "unionId": null,
     *             "scope": null,
     *             "tokenType": "bearer",
     *             "idToken": null,
     *             "macAlgorithm": null,
     *             "macKey": null,
     *             "code": null
     *         }
     *     }
     * }
     */

    /**
     * uuid
     */
    private String uuid;
    /**
     * 账号
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 博客
     */
    private String blog;
    /**
     * 公司
     */
    private String company;
    /**
     * 位置
     */
    private String location;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 备注
     */
    private String remark;
    /**
     * 性别 UNKNOWN - 未知
     */
    private String gender;
    /**
     * 来源：默认：GITHUB
     */
    private String source;
    /**
     * 令牌
     */
    private TokenBean token;

    /**
     * 令牌
     */
    @Data
    public static class TokenBean {

        private String accessToken;
        private int expireIn;
        private Object refreshToken;
        private Object uid;
        private Object openId;
        private Object accessCode;
        private Object unionId;
        private Object scope;
        private String tokenType;
        private Object idToken;
        private Object macAlgorithm;
        private Object macKey;
        private Object code;

    }
}
