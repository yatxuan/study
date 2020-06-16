package com.yat.config.shiro.jwt;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * <p>Description: 重写shiro的token类 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2019/9/10
 * @Time: 11:34
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
