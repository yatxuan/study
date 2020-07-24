package com.yat.config.shiro.redis;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yat.config.shiro.jwt.JwtUtil;
import com.yat.config.shiro.realm.ShiroRealm;
import com.yat.models.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.exception.PrincipalIdNullException;
import org.crazycake.shiro.exception.PrincipalInstanceException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>Description: shiro缓存-redis实现 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/29 19:08
 */
@Slf4j
public class RedisCache<K, V> implements Cache<K, V> {

    private JwtUtil jwtUtil;
    private RedisManager redisManager;
    private String keyPrefix = "";
    private int expire = 0;
    private String principalIdFieldName = RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME;

    RedisCache(RedisManager redisManager, JwtUtil jwtUtil, String prefix, int expire, String principalIdFieldName) {

        if (null == redisManager) {
            throw new IllegalArgumentException("redisManager cannot be null.");
        }
        if (null == jwtUtil) {
            throw new IllegalArgumentException("jwtUtil cannot be null.");
        }
        this.redisManager = redisManager;
        this.jwtUtil = jwtUtil;
        if (prefix != null && !"".equals(prefix)) {
            this.keyPrefix = prefix;
        }
        if (expire != -1) {
            this.expire = expire;
        }
        if (principalIdFieldName != null && !"".equals(principalIdFieldName)) {
            this.principalIdFieldName = principalIdFieldName;
        }

    }

    /**
     * 获取用户信息、用户权限
     *
     * @param key 、
     * @return 、
     * @throws CacheException 、
     */
    @Override
    public V get(K key) throws CacheException {
        log.info("get key [{}]", key);

        if (key == null) {
            return null;
        }

        try {
            String redisCacheKey = getRedisCacheKey(key);

            Object rawValue = redisManager.get(redisCacheKey);
            if (rawValue == null) {
                return null;
            }

            V value = (V) rawValue;
            return value;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
     * 存储用户信息、用户权限
     *
     * @param key   、
     * @param value 、
     * @return 、
     * @throws CacheException 、
     */
    @Override
    public V put(K key, V value) throws CacheException {
        log.info("put key [{}]", key);
        if (key == null) {
            log.warn("Saving a null key is meaningless, return value directly without call Redis.");
            return value;
        }
        try {
            String redisCacheKey = getRedisCacheKey(key);
            redisManager.set(redisCacheKey, value, expire);
            return value;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        log.info("remove key [{}]", key);
        if (key == null) {
            return null;
        }
        try {
            String redisCacheKey = getRedisCacheKey(key);
            Object rawValue = redisManager.get(redisCacheKey);
            V previous = (V) rawValue;
            redisManager.del(redisCacheKey);
            return previous;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    private String getRedisCacheKey(K key) {
        if (key == null) {
            return null;
        }
        return this.keyPrefix + getStringRedisKey(key);
    }

    private String getStringRedisKey(K key) {
        String redisKey;
        if (key instanceof PrincipalCollection) {
            redisKey = getRedisKeyFromPrincipalIdField((PrincipalCollection) key);
        } else {
            redisKey = jwtUtil.parseJwt(key.toString()).getSubject();
        }
        return redisKey;
    }

    private String getRedisKeyFromPrincipalIdField(PrincipalCollection key) {
        String redisKey;
        Object principalObject = key.getPrimaryPrincipal();
        Method pincipalIdGetter = null;
        Method[] methods = principalObject.getClass().getDeclaredMethods();
        for (Method m : methods) {
            boolean equalsName = RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME.equals(this.principalIdFieldName);
            if (equalsName && ("getAuthCacheKey".equals(m.getName()) || "getId".equals(m.getName()))) {
                pincipalIdGetter = m;
                break;
            }
            if (m.getName().equals("get" + this.principalIdFieldName.substring(0, 1).toUpperCase() + this.principalIdFieldName.substring(1))) {
                pincipalIdGetter = m;
                break;
            }
        }
        if (pincipalIdGetter == null) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName);
        }

        try {
            Object idObj = pincipalIdGetter.invoke(principalObject);
            if (idObj == null) {
                throw new PrincipalIdNullException(principalObject.getClass(), this.principalIdFieldName);
            }
            redisKey = idObj.toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName, e);
        }

        return redisKey;
    }


    @Override
    public void clear() throws CacheException {
        log.debug("clear cache");
        Set<String> keys = null;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            log.error("get keys error", e);
        }
        if (keys == null || keys.size() == 0) {
            return;
        }
        for (String key : keys) {
            redisManager.del(key);
        }
    }

    @Override
    public int size() {
        Long longSize = 0L;
        try {
            longSize = redisManager.scanSize(this.keyPrefix + "*");
        } catch (Exception e) {
            log.error("get keys error", e);
        }
        return longSize.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        Set<String> keys = null;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            log.error("get keys error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        Set<K> convertedKeys = new HashSet<>(16);
        for (String key : keys) {
            try {
                convertedKeys.add((K) key);
            } catch (Exception e) {
                log.error("deserialize keys error", e);
            }
        }
        return convertedKeys;
    }

    @Override
    public Collection<V> values() {
        Set<String> keys = null;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            log.error("get values error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        List<V> values = new ArrayList<V>(keys.size());
        for (String key : keys) {
            V value = null;
            try {
                value = (V) redisManager.get(key);
            } catch (Exception e) {
                log.error("deserialize values= error", e);
            }
            if (value != null) {
                values.add(value);
            }
        }
        return Collections.unmodifiableList(values);
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getPrincipalIdFieldName() {
        return principalIdFieldName;
    }

    public void setPrincipalIdFieldName(String principalIdFieldName) {
        this.principalIdFieldName = principalIdFieldName;
    }
}
