package com.yat.config.shiro.jwt;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * <p>Description: 这里一定要重写shiro的token类，不然会有bug </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 13:44
 */
@AllArgsConstructor
public class JwtToken implements AuthenticationToken {
    /**
     * Token
     */
    private String token;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
