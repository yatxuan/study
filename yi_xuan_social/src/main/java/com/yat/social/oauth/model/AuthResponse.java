package com.yat.social.oauth.model;

import com.yat.social.oauth.enums.AuthResponseStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一授权响应类
 *
 * @author Yat-Xuan
 * @since 1.8
 */
@Data
@Builder
public class AuthResponse<T> implements Serializable {
    /**
     * 授权响应状态码
     */
    private int code;

    /**
     * 授权响应信息
     */
    private String msg;

    /**
     * 授权响应数据，当且仅当 code = 2000 时返回
     */
    private T data;

    /**
     * 是否请求成功
     *
     * @return true or false
     */
    public boolean ok() {
        return this.code == AuthResponseStatus.SUCCESS.getCode();
    }
}
