package com.yat.config.shiro.jwt;

import cn.hutool.core.text.StrBuilder;
import com.google.gson.Gson;
import com.yat.common.exception.CustomException;
import com.yat.common.utils.StringUtils;
import com.yat.common.utils.ip.AddressUtils;
import com.yat.config.redis.RedisUtils;
import com.yat.models.entity.dto.authority.LoginUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;
import java.util.List;

import static com.yat.common.constant.CommonConstant.*;
import static com.yat.common.constant.JwtConstant.ONLINE_USER_LOGIN_TIMES;

/**
 * <p>Description: JWT令牌:这里加密的方式是把用户的账号进行加密，当做Key,把用户的登陆信息当做value 存储在redis中 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 13:42
 */
@Slf4j
@Data
@Configuration
@RequiredArgsConstructor
public class JwtUtil implements InitializingBean, HandlerExceptionResolver {

    /**
     * jwt有效时间
     */
    @Value("${jwt.expire}")
    private int expire;
    /**
     * 秘钥
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 存储在线用户的key值的前缀
     */
    @Value("${jwt.online.keyPrefix}")
    private String onlineKeyPrefix;
    /**
     * 存储在线用户的key值的前缀
     */
    @Value("${jwt.web.header}")
    private String header;
    /**
     * 存储在前端token的前缀
     */
    @Value("${jwt.web.tokenPrefix}")
    private String tokenPrefix;

    /**
     * 盐
     */
    private Key key;

    private final RedisUtils<String> redisUtils;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
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

        // 判断当前账号是否在其他客户端进行登陆
        if (redisUtils.hasKey(ONLINE_USER_LOGIN_TIMES + loginUser.getUsername())) {
            // 获取当前账号 客户端登陆地点的ip
            List<String> list = redisUtils.listRange(ONLINE_USER_LOGIN_TIMES + loginUser.getUsername());
            if (null != list && !list.isEmpty()) {
                // 判断用户是否为 重复登陆
                if (!list.contains(loginUser.getLogIp())) {
                    // 如果不是重复登陆，判断该账号是否允许多地点登陆
                    // (这里可以到数据库查询，也可以到配置文件中设置，每个账号的同时登陆人数)
                    // 判断 允许的最大的登陆人数 小于等于 当前已经登录的人数
                    if (loginUser.getLogNumber() <= list.size()) {
                        // 判断是否挤退别人
                        if (1 == loginUser.getSqueeze()) {
                            //  挤掉最先登录的用户: 获取当前账号，第一个客户端的ip
                            String fistLogIp = redisUtils.listGetIndex(ONLINE_USER_LOGIN_TIMES + loginUser.getUsername(), 0);
                            String ip = StringUtils.remove(fistLogIp, ".");

                            // 挤掉最先登录的用户: 把当前账号的客户端的ip进行覆盖
                            redisUtils.listUpdateIndex(ONLINE_USER_LOGIN_TIMES + loginUser.getUsername(),
                                    0, loginUser.getLogIp());
                            String onlineKey = onlineKeyPrefix + ip + loginUser.getUsername();
                            redisUtils.del(onlineKey);

                            // 在给退出的客户端一个友好提示
                            StrBuilder strBuilder = new StrBuilder("您的账号于");
                            strBuilder.append(loginUser.getLoginTime());
                            strBuilder.append("在另一台设备上进行登陆，已经被迫下线，若非本人操作，请立即修改密码");
                            String friendlyTips = onlineKeyPrefix + loginUser.getUsername() + ip;
                            redisUtils.set(friendlyTips, strBuilder.toString());

                        } else if (-1 == loginUser.getSqueeze()) {
                            throw new CustomException("当前您的账号在另一台设备上登陆，是否重新登陆？");
                        } else {
                            throw new CustomException("登陆失败！");
                        }
                    }
                } else {
                    log.info("重复登陆");
                }
            }
        }

        expire = expire > 0 ? expire : EXPIRE;
        String token = Jwts.builder()
                .setSubject(loginUser.getUsername())
                .claim("logIp", loginUser.getLogIp())
                .signWith(key, SignatureAlgorithm.HS512)
                // 因为token的时间单位为毫秒，所以这里要化单位为毫秒
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000))
                .compact();

        // 因为ip要作为redis的key值，这里去除小数点
        String ip = StringUtils.remove(loginUser.getLogIp(), ".");

        // 这里的key值 等于 前缀+ip+用户名 这里添加ip的作用是为了区分一个账号多个地点进行登录
        String onlineKey = onlineKeyPrefix + ip + loginUser.getUsername();
        String json = new Gson().toJson(loginUser);
        redisUtils.set(onlineKey, json, expire);
        // 记录每个账号同时在线人数
        // 先删除，在添加，防止重复添加
        redisUtils.listRemove(ONLINE_USER_LOGIN_TIMES + loginUser.getUsername(), 1L,
                loginUser.getLogIp());
        redisUtils.listRightPush(ONLINE_USER_LOGIN_TIMES + loginUser.getUsername(),
                loginUser.getLogIp(), expire);
        return token;
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
        final String requestHeader = request.getHeader(header);
        if (requestHeader != null && requestHeader.startsWith(tokenPrefix)) {
            // 当前用户登陆的ip
            String ipAddr = AddressUtils.getIpAddr(request);

            // 当前请求的接口名称
            String requestUrl = request.getRequestURI();

            String token = requestHeader.substring(tokenPrefix.length());
            // 查看redis是否包含用户的登陆消息，判断token是否正确，时间是否过期
            if (StringUtils.isNotBlank(token) && validateToken(token)) {
                LoginUser currUser = getLoginUser(token);

                if (null == currUser) {
                    log.error("用户不存在，令牌错误：token: '{}'，请重新登录,uri: '{}',IP: '{}'", token, requestUrl, ipAddr);
                    return null;
                }

                // 判断当前客户端的ip和token令牌中的ip是否一致，如果不一致，就说明当前客户端使用的ip是其他客户端的，不允许登陆
                if (!StringUtils.equals(ipAddr, currUser.getLogIp())) {
                    log.error("IP不一致，令牌错误：token: '{}'，请重新登录,\r\nuri: '{}',\nOriginal-IP'{}',\nIP: '{}'", token, requestUrl, currUser.getLogIp(), ipAddr);
                    return null;
                }
                log.info("set Authentication to security context for '{}', uri: '{}',IP: '{}'", currUser.getUsername(), requestUrl, ipAddr);
            } else {
                log.error("no valid JWT token found, uri: '{}',IP: '{}", requestUrl, ipAddr);
                return null;
            }
            return token;
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
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature.");
            return false;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.'{}'", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid.");
            return false;
        } catch (SignatureException e) {
            log.error("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted");
            return false;
        }
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public LoginUser getLoginUser(String token) {
        Claims claims = parseJwt(token);
        // 获取用户名
        String subject = claims.getSubject();
        // 获取ip
        String logIp = (String) getClaim(claims, "logIp");
        String ip = StringUtils.remove(logIp, ".");

        String message;

        if (redisUtils.hasKey(onlineKeyPrefix + ip + subject)) {
            return new Gson().fromJson(redisUtils.get(onlineKeyPrefix + ip + subject), LoginUser.class);
        } else {
            // 如果不存在，判断是否被别人挤出去了
            if (redisUtils.hasKey(onlineKeyPrefix + subject + ip)) {
                message = redisUtils.get(onlineKeyPrefix + subject + ip, 1);
            } else {
                message = "登陆状态已过期，请重新登陆";
            }
        }
        throw new CustomException(message);
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

    /**
     * 获取存储在token里面，自定义的key的数据
     *
     * @param token token数据
     * @param key   自定义key
     * @return key值对应的value
     */
    public String getClaim(String token, String key) {
        Claims claims = parseJwt(token);
        return claims.get(key).toString();
    }


    /**
     * 根据token 删除当前用户的所有权限数据
     *
     * @param token 用户令牌
     */
    public void remove(String token) {
        String userNem = parseJwt(token).getSubject();
        redisUtils.del(PREFIX_SHIRO_CACHE + AUTHORIZATION_CACHE + userNem,
                PREFIX_SHIRO_CACHE + AUTHENTICATION_CACHE + userNem);
    }


    /**
     * 根据token 删除所有用户的所有权限数据
     */
    public void remove() {
        List<String> authorizationCache = redisUtils.scan(PREFIX_SHIRO_CACHE + AUTHORIZATION_CACHE + "*");
        List<String> authenticationCache = redisUtils.scan(PREFIX_SHIRO_CACHE + AUTHENTICATION_CACHE + "*");
        authorizationCache.forEach(redisUtils::del);
        authenticationCache.forEach(redisUtils::del);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("-----------------------------------------------------");
        return null;
    }
}
