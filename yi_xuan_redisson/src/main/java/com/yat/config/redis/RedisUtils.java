package com.yat.config.redis;

import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.util.CollectionUtils.arrayToList;

/**
 * <p>Description: Redis工具类 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2019/9/4
 * @Time: 17:21
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings({"unused", "unchecked"})
public class RedisUtils<V> {

    private final RedisTemplate<String, V> redisTemplate;
    private final ValueOperations<String, V> valueOperations;
    private final HashOperations<String, String, V> hashOperations;
    private final ListOperations<String, V> listOperations;
    private final SetOperations<String, V> setOperations;
    private final ZSetOperations<String, V> zSetOperations;

    /**
     * 默认过期时长，单位：秒
     */
    private final static long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**
     * 不设置过期时长
     */
    private final static long NOT_EXPIRE = -1L;
    /**
     * 预警时间 单位（秒）
     */
    public final static long EARLY_WARNING_TIME = 60L;

    //=============================common============================

    public RedisTemplate<String, V> getRedisTemplate() {
        return redisTemplate;
    }


    /**
     * 判断redis是否启动或者是否挂掉
     *
     * @return true-未启动  false-启动
     */
    public boolean isRedisStartUp() {
        try {
            get("name");
            return false;
        } catch (RedisConnectionFailureException | RedisConnectionException e) {
            log.error("redis服务未启动，或redis服务器已挂掉");
            return true;
        }
    }

    /**
     * <p> 指定缓存失效时间 </p>
     * <p> expire key seconds </p>
     *
     * @param key    键
     * @param expire 时间(秒)
     */
    public void expire(String key, long expire) {
        try {
            if (expire > 0) {
                redisTemplate.expire(key, expire, SECONDS);
            }
        } catch (Exception e) {
            log.error("指定缓存失效时间报错，报错原因为{}", e.getMessage());
        }
    }

    /**
     * <p> 根据key 获取过期时间 </p>
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, SECONDS);
        return null == expire ? 0 : expire;
    }

    /**
     * <p> 判断key是否存在 <p>
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            Boolean hasKey = redisTemplate.hasKey(key);
            return null == hasKey ? false : hasKey;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p> 判断key是否存在 与 hasKey结果相反<p>
     *
     * @param key 键
     * @return true 不存在 false存在
     */
    public boolean notHasKey(String key) {
        return !hasKey(key);
    }

    /**
     * 查询redis里所有的key-精准查询
     *
     * @param pattern 查询条件,为空则查询所有
     * @param limit   每次查询匹配的数量
     * @return 包含key值的数据集
     */
    public Set<String> noLikeScan(String pattern, long limit) {
        if (StringUtils.isBlank(pattern)) {
            return scan(null, limit, false);
        }
        return scan(pattern, limit, false);
    }

    /**
     * 查询redis里所有的key-模糊查询
     *
     * @param pattern 查询条件,为空则查询所有
     * @param limit   每次查询匹配的数量
     * @return 包含key值的数据集
     */
    public Set<String> isLikeScan(String pattern, long limit) {
        if (StringUtils.isBlank(pattern)) {
            return scan(null, limit, false);
        }
        return scan(pattern, limit, true);
    }

    /**
     * 查询redis里所有的key-模糊查询
     *
     * @param pattern 查询条件,为空则查询所有
     * @return 包含key值的数据集
     */
    public Set<String> isLikeScan(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return scan(null, 50, false);
        }
        return scan(pattern, 50, true);
    }

    /**
     * 查找匹配key
     *
     * @param pattern key
     * @return /
     */
    public List<String> scan(String pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = Objects.requireNonNull(factory).getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<String> result = new ArrayList<>();
        while (cursor.hasNext()) {
            result.add(new String(cursor.next()));
        }
        try {
            RedisConnectionUtils.releaseConnection(rc, factory, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <p> 获取所有的key </p>
     * <p> scan cursor [MATCH pattern] [COUNT count] </p>
     * <p> scan 0 match A* count 20  </p>
     * <p> scan 0 第一次游标从0开始 </p>
     * <p> match A* 查询以A开头（或者包含A--->*A*）的{@code key}值 </p>
     * <p> count 20 查询数量为 20（这个20，不一定会查询出20个，只是以20唯一个查询数量的标准，
     * 查询的{@code key}值的数量有可能多，也可能少） </p>
     *
     * @param pattern 查询条件
     * @param limit   查询的数量
     * @param like    是否进行模糊查询
     * @return 返回redis里所有的key
     */
    private Set<String> scan(String pattern, long limit, boolean like) {

        if (limit == 0) {
            limit = 10000;
        }

        if (like) {
            pattern = StringUtils.replace(StringUtils.deleteWhitespace(pattern), "*", "");
            pattern = "*" + pattern + "*";
        } else {
            pattern = StringUtils.deleteWhitespace(pattern);
        }

        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(limit).build();
        StringRedisSerializer redisSerializer = (StringRedisSerializer) redisTemplate.getKeySerializer();
        Cursor<String> cursor = (Cursor<String>) redisTemplate.executeWithStickyConnection(
                (RedisCallback) redisConnection -> new ConvertingCursor<>(
                        redisConnection.scan(scanOptions), redisSerializer::deserialize
                )
        );
        Set<String> keys = new HashSet<>(16);
        assert cursor != null;
        while (cursor.hasNext()) {
            keys.add(cursor.next());
        }
        return keys;
    }

    /**
     * <p> 删除缓存 <p>
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(arrayToList(key));
            }
        }
    }


    //============================String=============================
    // 应用场景：String是最常用的一种数据类型，普通的key/ value 存储都可以归为此类.

    public ValueOperations<String, V> getValueOperations() {
        return valueOperations;
    }

    /**
     * <p> 普通缓存获取 <p>
     * <p> get key <p>
     *
     * @param key 键
     * @return 值-String（Json
     */
    public V get(String key) {
        return (key == null) ? null : get(key, NOT_EXPIRE);
    }

    /**
     * <p> 普通缓存获取，并设置一个过期时间 <p>
     *
     * @param key    键
     * @param expire 过期时间
     * @return 值-String（Json
     */
    public V get(String key, long expire) {
        V value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, SECONDS);
        }
        return value;
    }

    /**
     * <p> 普通缓存放入 <p>
     * <p> set key value <p>
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, V value) {
        try {
            return set(key, value, DEFAULT_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * <p> 普通缓存放入并设置时间 <p>
     *
     * @param key    键
     * @param value  值
     * @param expire 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, V value, long expire) {
        try {
            valueOperations.set(key, value);
            if (expire != NOT_EXPIRE) {
                expire(key, expire);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p> 递增 <p>
     * <p> inCr  key count <p>
     *
     * @param key 键
     * @return 递增后的值
     */
    public long increment(String key) {
        return increment(key, 1);
    }

    /**
     * <p> 递增 <p>
     * <p> inCrBy  key count <p>
     *
     * @param key   键
     * @param count 要增加几(大于0)
     * @return 递增后的值
     */
    public long increment(String key, long count) {
        if (count < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        Long increment = valueOperations.increment(key, count);
        return null == increment ? 0 : increment;
    }

    /**
     * <p> 递减 <p>
     * <p> deCr key count <p>
     *
     * @param key 键
     * @return 递减后的值
     */
    public long decrement(String key) {
        return decrement(key, 1);
    }

    /**
     * <p> 递减 <p>
     * <p> deCrBy key count <p>
     *
     * @param key   键
     * @param count 要减少几(小于0)
     * @return 递减后的值
     */
    public long decrement(String key, long count) {
        if (count < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        Long increment = valueOperations.decrement(key, count);
        return null == increment ? 0 : increment;
    }

    //================================Hash散列=================================

    public HashOperations<String, String, V> getHashOperations() {
        return hashOperations;
    }

    /**
     * <p> 获取散列 {@code hashKey} ,{@code key}为{@code item}的值 <p>
     * <p> hGet key item <p>
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public V hget(String key, String item) {
        return hashOperations.get(key, item);
    }

    /**
     * <p> 获取hashKey对应的所有键值 <p>
     * <p> hGetAll key <p>
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, V> hmget(String key) {
        return hashOperations.entries(key);
    }

    /**
     * <p> HashSet <p>
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, V> map) {
        try {
            hashOperations.putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p> HashSet 并设置时间 <p>
     *
     * @param key    键
     * @param map    对应多个键值
     * @param expire 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmSet(String key, Map<String, V> map, long expire) {
        try {
            hashOperations.putAll(key, map);
            if (expire > 0) {
                expire(key, expire);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p> 向一张hash表中放入数据,如果不存在将创建 <p>
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, V value) {
        try {
            hashOperations.put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p> 向一张hash表中放入数据,如果不存在将创建 <p>
     *
     * @param key    键
     * @param item   项
     * @param value  值
     * @param expire 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, V value, long expire) {
        try {
            hashOperations.put(key, item, value);
            if (expire > 0) {
                expire(key, expire);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p> 删除hash表中的值 <p>
     * <p> hDel key  field [field ...] <p>
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public long hDel(String key, V... item) {
        try {
            Long del = hashOperations.delete(key, item);
            return null == del ? 0 : del;
        } catch (Exception e) {
            log.error("删除hash表的值，失败--->{}", e.getMessage());
            return 0;
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return hashOperations.hasKey(key, item);
    }

    /**
     * <p> hash递增 如果不存在,就会创建一个 并把新增后的值返回 <p>
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return 新增后的值
     */
    public double hIncr(String key, String item, double by) {
        return hashOperations.increment(key, item, by);
    }

    /**
     * <p> hash递减 <p>
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return 递减后的值
     */
    public double hDecr(String key, String item, double by) {
        return hashOperations.increment(key, item, -by);
    }


    //============================set集合=============================

    public SetOperations<String, V> getSetOperations() {
        return setOperations;
    }

    /**
     * <p> 根据key获取Set中的所有值 </p>
     * <p> sMembers key</p>
     *
     * @param key 键
     * @return 值
     */
    public Set<V> sGet(String key) {
        try {
            return setOperations.members(key);
        } catch (Exception e) {
            log.error("集合获取数据失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 随机获取Set中的数量值 </p>
     * <p> (count为正数时，获取的数据不可重复，count为负数时，获取的数据可重复) <p>
     * <p> sRandMember key [count]</p>
     *
     * @param key   键
     * @param count 获取的数量值
     * @return 值
     */
    public List<V> setMemberCount(String key, Integer count) {
        try {
            if (null == count || count == 0) {
                count = 1;
            } else if (count < 0) {
                return setOperations.randomMembers(key, count);
            }

            Set<V> objects = setOperations.distinctRandomMembers(key, count);
            return null == objects ? null : new ArrayList<>(objects);

        } catch (Exception e) {
            log.error("集合获取数据失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 根据value从一个set中查询,是否存在 </p>
     * <p> sIsMember key value </p>
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, V value) {
        try {
            Boolean bool = setOperations.isMember(key, value);
            return null != bool ? bool : false;
        } catch (Exception e) {
            log.error("集合查询数据失败----->{}", e.getMessage());
            return false;
        }
    }

    /**
     * <p> 将数据放入set缓存 </p>
     * <p> sAdd key value [value ...] </p>
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long setAdd(String key, V... values) {
        try {
            return setAddTime(key, DEFAULT_EXPIRE, values);
        } catch (Exception e) {
            log.error("集合添加数据失败----->{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> 将set数据放入缓存,设置集合的有效时间 </p>
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long setAddTime(String key, long time, V... values) {
        try {
            Long count = setOperations.add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return null == count ? 0 : count;
        } catch (Exception e) {
            log.error("集合获取数据，设置集合的有效时间失败----->{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> 获取set缓存的长度 </p>
     * <p> sCard key </p>
     *
     * @param key 键
     * @return 长度大小
     */
    public long setCard(String key) {
        try {
            Long size = setOperations.size(key);
            return null == size ? 0 : size;
        } catch (Exception e) {
            log.error("集合获取数据长度失败----->{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> 将集合{@code outKey}里面的元素{@code value}从移动到{@code inKey} </p>
     * <p> sMove outKey inKey value </p>
     *
     * @param outKey 移出元素的集合
     * @param inKey  移入元素的集合
     * @param value  需要移动的元素
     * @return 、
     */
    public boolean sMove(String outKey, String inKey, V value) {
        try {
            Boolean move = setOperations.move(outKey, value, inKey);
            return null == move ? false : move;
        } catch (Exception e) {
            log.error("集合删除元素失败----->{}", e.getMessage());
            return false;
        }
    }

    /**
     * <p> Set并集：返回多个集合的并集 </p>
     * <p> sUnion  key [key ...] </p>
     *
     * @param key      集合 1
     * @param otherKey 集合 2
     * @return 返回两个集合的并集（两个集合的所有元素）
     */
    public Set<V> union(String key, String otherKey) {
        try {
            return setOperations.union(key, otherKey);
        } catch (Exception e) {
            log.error("并集处理失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set并集：返回多个集合的并集 </p>
     * <p> sUnion  key [key ...] </p>
     *
     * @param otherKeys 多个集合
     * @return 返回多个集合的并集（两个集合的所有元素）
     */
    public Set<V> union(Collection<String> otherKeys) {
        try {
            int count = 2;
            if (otherKeys.size() < count) {
                log.error("集合长度不能低于小于2");
            }
            String key = otherKeys.stream().findFirst().orElse("");
            return setOperations.union(key, otherKeys);
        } catch (Exception e) {
            log.error("并集处理失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set并集：返回多个集合的并集，将并集存储在{@code deistKey}中  </p>
     * <p> sUnionStore deistKey key [key ...] </p>
     *
     * @param key      集合 1
     * @param otherKey 集合 2
     * @param deistKey 存储并集的集合
     * @return 并集的长度
     */
    public Long unionAndStore(String key, String otherKey, String deistKey) {
        try {
            return setOperations.unionAndStore(key, otherKey, deistKey);
        } catch (Exception e) {
            log.error("并集存储失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set并集：返回多个集合的并集，将并集存储在{@code deistKey}中 </p>
     * <p> sUnionStore deistKey key [key ...] </p>
     *
     * @param otherKeys 多个集合
     * @param deistKey  存储并集的集合
     * @return 并集的长度
     */
    public Long unionAndStore(Collection<String> otherKeys, String deistKey) {
        try {
            String key = otherKeys.stream().findFirst().orElse("");
            return setOperations.unionAndStore(key, otherKeys, deistKey);
        } catch (Exception e) {
            log.error("并集存储失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set交集：返回多个集合的交集 </p>
     * <p> sInter key [key ...] </p>
     *
     * @param key      集合 1
     * @param otherKey 集合 2
     * @return 返回两个集合共有的元素
     */
    public Set<V> intersect(String key, String otherKey) {
        try {
            return setOperations.intersect(key, otherKey);
        } catch (Exception e) {
            log.error("交集处理失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set交集：返回多个集合的交集 </p>
     * <p> sInter key [key ...] </p>
     *
     * @param otherKeys 多个集合
     * @return 返回多个集合共有的元素
     */
    public Set<V> intersect(Collection<String> otherKeys) {
        try {
            int count = 2;
            if (otherKeys.size() < count) {
                log.error("集合长度不能低于小于2");
            }
            String key = otherKeys.stream().findFirst().orElse("");
            return setOperations.intersect(key, otherKeys);
        } catch (Exception e) {
            log.error("交集处理失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set交集：返回多个集合的交集,并将交集存储在集合{@code deistKey}中  </p>
     * <p> sInterStore deistKey key [key ...] </p>
     *
     * @param key      集合 1
     * @param otherKey 集合 2
     * @param deistKey 存储交集的集合
     * @return 返回两个集合共有的元素
     */
    public Long intersectAndStore(String key, String otherKey, String deistKey) {
        try {
            return setOperations.intersectAndStore(key, otherKey, deistKey);
        } catch (Exception e) {
            log.error("交集存储失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set交集：返回多个集合的交集，并将交集存储在集合{@code deistKey}中 </p>
     * <p> sInterStore deistKey key [key ...] </p>
     *
     * @param otherKeys 多个集合
     * @param deistKey  存储交集的集合
     * @return 返回交集的长度
     */
    public Long intersect(Collection<String> otherKeys, String deistKey) {
        try {
            String key = otherKeys.stream().findFirst().orElse("");
            return setOperations.intersectAndStore(key, otherKeys, deistKey);
        } catch (Exception e) {
            log.error("交集存储失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set差集: 返回集合 {@code key} 中存在，但是 {@code otherKey} 中不存在的数据集合 </p>
     * <p> sDiff key [key ...] </p>
     *
     * @param key      集合 1
     * @param otherKey 集合 2
     * @return 返回差集
     */
    public Set<V> difference(String key, String otherKey) {
        try {
            return setOperations.difference(key, otherKey);
        } catch (Exception e) {
            log.error("差集处理失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set差集: 返回集合 {@code key} 中存在，但是 {@code otherKeys} 中不存在的数据集合 </p>
     * <p> sDiff key [key ...] </p>
     *
     * @param key       集合 1
     * @param otherKeys 多个集合
     * @return 返回差集
     */
    public Set<V> difference(String key, Collection<String> otherKeys) {
        try {
            return setOperations.difference(key, otherKeys);
        } catch (Exception e) {
            log.error("差集处理失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set差集: 将集合 {@code key} 和集合 {@code otherKey} 的差集，存储在 {@code deistKey}里 </p>
     * <p> sDiffStore deistKey key [key ...] </p>
     *
     * @param key      集合 1
     * @param otherKey 集合 2
     * @param deistKey 存储差集的集合
     * @return 返回差集的长度
     */

    public Long differenceAndStore(String key, String otherKey, String deistKey) {
        try {
            return setOperations.differenceAndStore(key, otherKey, deistKey);
        } catch (Exception e) {
            log.error("差集存储失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> Set差集: 将集合 {@code key} 和集合 {@code otherKeys} 的差集，存储在 {@code deistKey}里 </p>
     * <p> sDiffStore deistKey key [key ...] </p>
     *
     * @param key       集合
     * @param otherKeys 多个集合
     * @param deistKey  存储差集的集合
     * @return 返回差集的长度
     */
    public Long differenceAndStore(String key, Collection<String> otherKeys, String deistKey) {
        try {
            return setOperations.differenceAndStore(key, otherKeys, deistKey);
        } catch (Exception e) {
            log.error("差集存储失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 移除值为value的 <p>
     * <p> sRem key value [value ...] <p>
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = setOperations.remove(key, values);
            return null == count ? 0 : count;
        } catch (Exception e) {
            log.error("集合删除元素失败----->{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> 随机删除集合里指定数量的元素 <p>
     * <p> sPop key [count] <p>
     *
     * @param key   键
     * @param count 需要删除的元素数量
     * @return 被删除的元素
     */
    public List<V> setRemove(String key, long count) {
        try {
            return setOperations.pop(key, count);
        } catch (Exception e) {
            log.error("集合删除{}个元素失败----->{}", count, e.getMessage());
            return null;
        }
    }

    //===============================zSet有序集合=================================

    public ZSetOperations<String, V> getZSetOperations() {
        return zSetOperations;
    }

    /**
     * <p> 获取zSet缓存的内容 </p>
     * <p> zRange key start stop</p>
     * <p> 0 到 -1代表所有值</p>
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 返回 value 有序的集合，score小的在前面
     */
    public Set<V> zSetGet(String key, long start, long end) {
        try {
            return zSetOperations.range(key, start, end);
        } catch (Exception e) {
            log.error("有序集合获取数据失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 查询集合中指定顺序的值 </p>
     * <p> zRevRange key start stop </p>
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 返回 value 有序的集合中，score大的在前面
     */
    public Set<V> zSetRevRange(String key, int start, int end) {
        try {
            return zSetOperations.reverseRange(key, start, end);
        } catch (Exception e) {
            log.error("有序集合获取数据失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 查询集合中指定顺序的值和score，0, -1 表示获取全部的集合内容 </p>
     * <p> zRange key start end withScores </p>
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 根据score范围，返回元素
     */
    public Set<ZSetOperations.TypedTuple<V>> rangeWithScore(String key, int start, int end) {
        try {
            return zSetOperations.rangeWithScores(key, start, end);
        } catch (Exception e) {
            log.error("有序集合获取数据失败----->{}", e.getMessage());
            return null;
        }
    }


    /**
     * <p> 根据score的值，来获取满足条件的集合 </p>
     * <p> zRangeByScore key min max </p>
     *
     * @param key 键
     * @param min 最小score
     * @param max 最大score
     * @return 返回 value 有序的集合中，score小的在前面
     */
    public Set<V> sortRange(String key, int min, int max) {
        try {
            return zSetOperations.rangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("有序集合获取数据失败----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 添加一个元素, zSet与set最大的区别就是每个元素都有一个score，因此有个排序的辅助功能; </p>
     * <p> zAdd key score value </p>
     *
     * @param key   键
     * @param value 值
     * @param score 排序数字
     */
    public boolean zSetAdd(String key, V value, double score) {
        try {
            Boolean add = zSetOperations.add(key, value, score);
            return null == add ? false : add;
        } catch (Exception e) {
            log.error("有序集合添加数据----->{}", e.getMessage());
            return false;
        }
    }

    /**
     * <p> 删除元素 </p>
     * <p> zRem key value [value ...]</p>
     *
     * @param key    键
     * @param values 值
     * @return 返回删除的数量
     */
    public long zSetRemove(String key, Object... values) {
        try {
            Long count = zSetOperations.remove(key, values);
            return null == count ? 0 : count;
        } catch (Exception e) {
            log.error("有序集合删除数据错误----->{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> 修改score的增加or减少 </p>
     * <p> zInCrBy key score value </p>
     *
     * @param key   键
     * @param value 值
     * @param score 排序数字
     * @return 返回新增or减少后的大小
     */
    public Double incrScore(String key, V value, double score) {
        try {
            return zSetOperations.incrementScore(key, value, score);
        } catch (Exception e) {
            log.error("有序集合修改score数据错误----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 查询value对应的score </p>
     * <p> zScore key value </p>
     *
     * @param key   键
     * @param value 值
     * @return 排序的值-score
     */
    public Double score(String key, String value) {
        try {
            return zSetOperations.score(key, value);
        } catch (Exception e) {
            log.error("有序集合获取score数据错误----->{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 判断value在zSet中的排名 </p>
     * <p> zRank key value </p>
     *
     * @param key   键
     * @param value 值
     * @return 排列的名称 第一位为 0
     */
    public Long zSetRank(String key, String value) {
        try {
            return zSetOperations.rank(key, value);
        } catch (Exception e) {
            log.error("有序集合获取排名失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 获取集合的长度 </p>
     * <p> zCard key </p>
     *
     * @param key 键
     * @return 长度大小
     */
    public long zSetSize(String key) {
        try {
            Long zSetSize = zSetOperations.zCard(key);
            return null == zSetSize ? 0 : zSetSize;
        } catch (Exception e) {
            log.error("有序集合获取数据长度失败：{}", e.getMessage());
            return 0;
        }
    }

    //===============================list列表=================================

    public ListOperations<String, V> getListOperations() {
        return listOperations;
    }

    /**
     * <p> 获取list缓存的内容（所有数据） </p>
     * <p> lRange key start stop </p>
     *
     * @param key 键
     * @return 列表数据
     */
    public List<V> listRange(String key) {
        try {
            return listOperations.range(key, 0, -1);
        } catch (Exception e) {
            log.error("List列表获取数据内容失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 获取list缓存的内容(指定长度) </p>
     * <p> lRange key start stop </p>
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return 列表数据
     */
    public List<V> listRange(String key, long start, long end) {
        try {
            return listOperations.range(key, start, end);
        } catch (Exception e) {
            log.error("List列表获取数据内容失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> 获取list缓存的长度 </p>
     * <p> lLen key </p>
     *
     * @param key 键
     * @return 长度值
     */
    public long listSize(String key) {
        try {
            Long size = listOperations.size(key);
            return null == size ? 0 : size;
        } catch (Exception e) {
            log.error("List列表获取长度大小失败：{}", e.getMessage());
            return 0;
        }
    }


    /**
     * <p> 通过索引 获取list中的值 </p>
     * <p> lIndex key index </p>
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 放回坐标对应的值
     */
    public V listGetIndex(String key, long index) {
        try {
            return listOperations.index(key, index);
        } catch (Exception e) {
            log.error("List列表通过索引获取数据失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * <p> list存储 </p>
     * <p> 从右边（后面）插入数据 </p>
     *
     * @param key   键
     * @param value 值
     * @return 返回当前集合的长度
     */
    public long listRightPush(String key, V value) {
        try {
            Long listSize = listOperations.rightPush(key, value);
            return null == listSize ? 0 : listSize;
        } catch (Exception e) {
            log.error("List列表从右插入数据失败：{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> list存储，并设置列表的有效时间 </p>
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 返回当前集合的长度
     */
    public long listRightPush(String key, V value, long time) {
        try {
            Long listSize = listOperations.rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return null == listSize ? 0 : listSize;
        } catch (Exception e) {
            log.error("List列表从右插入数据, 设置有效时间失败：{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> list存储 </p>
     *
     * @param key   键
     * @param value 值
     * @return 返回当前集合的长度
     */
    public long listRightPush(String key, List<V> value) {
        try {
            Long listSize = listOperations.rightPushAll(key, value);
            return null == listSize ? 0 : listSize;
        } catch (Exception e) {
            log.error("List列表从右插入数据, 设置有效时间失败：{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> list存储 </p>
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 返回当前集合的长度
     */
    public long listRightPush(String key, List<V> value, long time) {
        try {
            Long listSize = listOperations.rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return null == listSize ? 0 : listSize;
        } catch (Exception e) {
            log.error("List列表从右插入数据, 设置有效时间失败：{}", e.getMessage());
            return 0;
        }
    }

    /**
     * <p> 根据索引修改list中的某条数据 </p>
     * <p> lSet key index value </p>
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true-成功
     */
    public boolean listUpdateIndex(String key, long index, V value) {
        try {
            listOperations.set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("List列表根据索引修改数据失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * <p> 移除N个值为value </p>
     * <p> lRem key count value </p>
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long listRemove(String key, long count, V value) {
        try {
            Long remove = listOperations.remove(key, count, value);
            return null == remove ? 0 : remove;
        } catch (Exception e) {
            log.error("List列表删除数据失败：{}", e.getMessage());
            return 0;
        }
    }

}
