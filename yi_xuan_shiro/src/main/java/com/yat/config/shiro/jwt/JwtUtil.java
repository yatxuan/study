package com.yat.config.shiro.jwt;

import cn.hutool.core.util.IdUtil;
import com.yat.common.constant.CommonConstant;
import com.yat.common.constant.RedisConstant;
import com.yat.common.exception.CustomUnauthorizedException;
import com.yat.common.utils.RedisUtils;
import com.yat.common.utils.StringUtils;
import com.yat.modules.authority.dto.LoginUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;

/**
 * <p>Description: JWT令牌 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2019/9/6
 * @Time: 13:46
 */
@Slf4j
@Data
@Configuration
@RequiredArgsConstructor
public class JwtUtil implements InitializingBean {

    /**
     * 盐
     */
    private Key key;

    private final RedisUtils<LoginUser> redisUtils;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(CommonConstant.BASE_64_SECRET);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 创建令牌
     * token里是不存储任何和用户有关的任何书籍，用户的登陆数据存储在redis中，过期时间为2小时，token里则存储 获取redis值的key
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String subject = IdUtil.randomUUID();
        String compact = Jwts.builder()
                .setSubject(subject)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        // 吧当前登陆的用户存入redis，过期时间2小时
        redisUtils.set(RedisConstant.ONLINE_KEY + subject, loginUser, 60 * 60 * 2L);
        return compact;
    }

    /**
     * 解析JWT
     *
     * @param jwtStr 令牌
     * @return 解析后的数据
     */
    public Claims parseJwt(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

    /**
     * 从Request域中获取token
     *
     * @param request 、
     * @return 、
     */
    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(CommonConstant.HEADER);
        if (requestHeader != null && requestHeader.startsWith(CommonConstant.TOKEN_PREFIX)) {
            return requestHeader.substring(7);
        }
        return null;
    }

    /**
     * 判断token是否正确
     *
     * @param token 令牌
     * @return /
     */
    public boolean validateToken(String token) {
        try {
            parseJwt(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature.");
            throw new CustomUnauthorizedException("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw new CustomUnauthorizedException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
            throw new CustomUnauthorizedException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new CustomUnauthorizedException("JWT token compact of handler are invalid.");
        }
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public LoginUser getLoginUser(String token) {
        String subject = parseJwt(token).getSubject();
        if (redisUtils.hasKey(RedisConstant.ONLINE_KEY + subject)) {
            return redisUtils.get(RedisConstant.ONLINE_KEY + subject, LoginUser.class);
        }
        return new LoginUser();
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotBlank(token)) {
            return getLoginUser(token);
        }
        return null;
    }

    /**
     * 获取存储在token里面，自定义的key的数据
     *
     * @param claims token数据
     * @param key    自定义key
     * @return key值对应的value
     */
    public Object getClaim(Claims claims, String key) {
        return claims.get(key);
    }


}
