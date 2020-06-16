package com.yat.config.redis;

import com.yat.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    private final RedisUtils<Integer> redisUtils;

    /**
     * 限流时： 最大请求数量
     */
    private final static String MAX_COUNT_KEY = "BUCKET:MAX_COUNT:";
    /**
     * 限流时： 当前请求数量
     */
    private final static String CURR_COUNT_KEY = "BUCKET:CURR_COUNT:";


    /**
     * 限流方法
     *
     * @param point   key
     * @param limit   限流次数
     * @param timeout 限流时间
     * @return 返回 true 表示进行限流，返回false，表示继续往下执行操作
     */
    public boolean acquireToken(String point, int limit, long timeout) {

        // 最大请求数量
        final String maxCountKey = MAX_COUNT_KEY + point;
        // 当前请求数量
        final String currCountKey = CURR_COUNT_KEY + point;

        try {
            // 无效的限流值 返回token
            if (limit <= 0 || timeout <= 0) {
                return false;
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
                return false;
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
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("限流出错，请检查Redis运行状态\n" + e.toString());
            return false;
        }
        return true;
    }
}

