package com.yat.config.shiro.redis;

import com.yat.config.shiro.jwt.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.yat.common.constant.CommonConstant.PREFIX_SHIRO_CACHE;

/**
 * <p>Description: cacheManager 缓存 redis实现 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/29 18:34
 */
@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class RedisCacheManager implements CacheManager {

    /**
     * fast lookup by name map
     */
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    private final RedisManager redisManager;
    private final JwtUtil jwtUtil;

    /**
     * expire time in seconds
     */
    private static final int DEFAULT_EXPIRE = 1800;
    private int expire = DEFAULT_EXPIRE;

    /**
     * The Redis key prefix for caches
     */
    public static final String DEFAULT_CACHE_KEY_PREFIX = PREFIX_SHIRO_CACHE;
    private String keyPrefix = DEFAULT_CACHE_KEY_PREFIX;

    public static final String DEFAULT_PRINCIPAL_ID_FIELD_NAME = "authCacheKey_or_id";
    private String principalIdFieldName = DEFAULT_PRINCIPAL_ID_FIELD_NAME;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        log.debug("get cache, name={}", name);

        Cache cache = caches.get(name);

        if (cache == null) {
            cache = new RedisCache<K, V>(redisManager, jwtUtil, keyPrefix + name, expire, principalIdFieldName);
            caches.put(name, cache);
        }
        return cache;
    }
}
