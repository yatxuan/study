package com.yat.reptile.rule;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.OS;
import com.yat.common.util.ip.AddressUtils;
import com.yat.reptile.config.AntiReptileProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Yat-Xuan
 * @since 2019/7/8
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "anti.reptile.manager.ip-rule", value = "enabled", havingValue = "true", matchIfMissing = true)
public class IpRule extends AbstractRule {

    private final RedissonClient redissonClient;
    private final AntiReptileProperties properties;

    private static final String RATE_LIMITER_COUNT_PREFIX = "rate:limiter:request:count:";
    private static final String RATE_LIMITER_EXPIRATION_TIME = "rate:limiter:expiration:time:";
    private static final String RATE_LIMITER_HIT_CRAWLER_STRATEGY = "rate:limiter:hit:crawler:strategy";

    @Override
    protected boolean doExecute(HttpServletRequest request, HttpServletResponse response) {
        // ip地址
        String ipAddress = AddressUtils.getIpAddr(request);
        List<String> ignoreIpList = properties.getIpRule().getIgnoreIp();
        if (ignoreIpList != null && ignoreIpList.size() > 0) {
            for (String ignoreIp : ignoreIpList) {
                if (ignoreIp.endsWith("*")) {
                    ignoreIp = ignoreIp.substring(0, ignoreIp.length() - 1);
                }
                if (ipAddress.startsWith(ignoreIp)) {
                    return false;
                }
            }
        }
        // 接口路径
        String requestUrl = request.getRequestURI();
        // 毫秒，默认5000
        int expirationTime = properties.getIpRule().getExpirationTime();
        //最高expirationTime时间内请求数
        int requestMaxSize = properties.getIpRule().getRequestMaxSize();

        RAtomicLong rRequestCount = redissonClient.getAtomicLong(concat(RATE_LIMITER_COUNT_PREFIX, requestUrl, ipAddress));
        RAtomicLong rExpirationTime = redissonClient.getAtomicLong(concat(RATE_LIMITER_EXPIRATION_TIME, requestUrl, ipAddress));
        if (!rExpirationTime.isExists()) {
            // rExpirationTime不存在
            rRequestCount.set(0L);
            rExpirationTime.set(0L);
            rExpirationTime.expire(expirationTime, TimeUnit.MILLISECONDS);
        } else {
            RMap<String, String> rHitMap = redissonClient.getMap(RATE_LIMITER_HIT_CRAWLER_STRATEGY);
            if ((rRequestCount.incrementAndGet() > requestMaxSize) || rHitMap.containsKey(ipAddress)) {
                //触发爬虫策略 ，默认10天后可重新访问
                long lockExpire = properties.getIpRule().getLockExpire();
                rExpirationTime.expire(lockExpire, TimeUnit.SECONDS);
                //保存触发来源
                rHitMap.put(ipAddress, requestUrl);
                OS os = AddressUtils.getOs(request);
                boolean mobile = AddressUtils.isMobile(request);
                String browser = AddressUtils.getBrowser(request) + ":" + AddressUtils.getBrowserVersion(request);
                String engine = AddressUtils.getEngine(request) + ":" + AddressUtils.getEngineVersion(request);
                log.info("Intercepted request, getWeekDay: {}, isMobile: {}, browser: {}, engine: {}, uri: {}, ip：{}, city: {}, os: {} request :{}, times in {} ms。Automatically unlock after {} seconds",
                        AddressUtils.getWeekDay(), mobile, browser, engine, requestUrl, ipAddress, AddressUtils.getCityInfo(ipAddress), os.getName(), requestMaxSize, expirationTime, lockExpire);
                return true;
            }
        }
        return false;
    }

    /**
     * 重置已记录规则
     *
     * @param request        请求
     * @param realRequestUri 原始请求uri
     */
    @Override
    public void reset(HttpServletRequest request, String realRequestUri) {
        String ipAddress = AddressUtils.getIpAddr(request);
        // 重置计数器
        int expirationTime = properties.getIpRule().getExpirationTime();

        RAtomicLong rRequestCount = redissonClient.getAtomicLong(concat(RATE_LIMITER_COUNT_PREFIX, realRequestUri, ipAddress));
        RAtomicLong rExpirationTime = redissonClient.getAtomicLong(concat(RATE_LIMITER_EXPIRATION_TIME, realRequestUri, ipAddress));
        rRequestCount.set(0L);
        rExpirationTime.set(0L);
        rExpirationTime.expire(expirationTime, TimeUnit.MILLISECONDS);
        //清除记录
        RMap rHitMap = redissonClient.getMap(RATE_LIMITER_HIT_CRAWLER_STRATEGY);
        rHitMap.remove(ipAddress);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    private String concat(String... str) {
        return StrUtil.concat(true, str);
    }
}
