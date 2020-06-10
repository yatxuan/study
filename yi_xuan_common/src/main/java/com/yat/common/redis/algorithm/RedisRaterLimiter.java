package com.yat.common.redis.algorithm;

import com.yat.common.redis.RedisUtils;
import com.yat.common.refactoring.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 令牌桶算法限流 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/13 17:38
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisRaterLimiter {

    /**
     * 限流时： 最大请求数量
     */
    private final static String MAX_COUNT_KEY = "BUCKET:MAX_COUNT:";
    /**
     * 限流时： 当前请求数量
     */
    private final static String CURR_COUNT_KEY = "BUCKET:CURR_COUNT:";

    private final RedisUtils<Integer> redisUtils;

    public String acquireToken(String point, int limit, long timeout) {

        // 最大请求数量
        String maxCountKey = MAX_COUNT_KEY + point;
        // 当前请求数量
        String currCountKey = CURR_COUNT_KEY + point;

        try {
            // 令牌值
            String token = "T";
            // 无效的限流值 返回token
            if (limit <= 0 || timeout <= 0) {
                return token;
            }
            String valueMaxCount = redisUtils.get(maxCountKey);
            String currCount = redisUtils.get(currCountKey);
            // 初始
            if (StringUtils.isBlank(valueMaxCount) && StringUtils.isBlank(currCount)) {
                // 计数加1
                redisUtils.increment(currCountKey);
                redisUtils.expire(currCountKey, timeout);
                // 总数
                redisUtils.set(maxCountKey, limit, timeout);
                return token;
            } else if (StringUtils.isNotBlank(valueMaxCount) && StringUtils.isNotBlank(currCount)) {
                // 判断是否超过限制
                if (StringUtils.isNotBlank(currCount) && Integer.valueOf(currCount) < Integer.valueOf(valueMaxCount)) {
                    // 计数加1
                    redisUtils.increment(currCountKey);
                    // 避免key失效 上述语句未设置失效时间
                    if (redisUtils.getExpire(currCountKey) == -1) {
                        log.info("当前请求名称：{}", currCount);
                        redisUtils.del(currCountKey);
                    }
                    return token;
                }
            } else {
                return token;
            }
        } catch (Exception e) {
            log.error("限流出错，请检查Redis运行状态\n" + e.toString());
        }
        return null;
    }
}

