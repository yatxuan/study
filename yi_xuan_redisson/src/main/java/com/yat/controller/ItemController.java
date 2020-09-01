package com.yat.controller;

import com.sun.istack.internal.NotNull;
import com.yat.config.redis.RedisUtils;
import com.yat.config.redis.RedissonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>Description: redisson分布式锁并发测试 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/23 14:50
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final static String ITEM_COUNT = "item:count";
    private final static String LOCK_KEY = "item:count:lock";

    private final RedisUtils<Integer> redisUtils;
    private final RedissonUtils redissonUtils;


    /**
     * 初始化商品数量
     *
     * @param value 数量
     * @return 、
     */
    @RequestMapping("/setCount")
    public String setCount(Integer value) {
        redisUtils.set(ITEM_COUNT, value);
        return "success";
    }

    /**
     * 查询商品数量
     *
     * @return 、
     */
    @RequestMapping("/getCount")
    public Integer getCount() {
        return redisUtils.get(ITEM_COUNT);
    }

    /**
     * 模拟秒杀抢购：tryLock用法
     *
     * @return 、
     */
    @RequestMapping("/tryLock")
    public String tryLock() {
        String result;
        if (redissonUtils.tryLock(LOCK_KEY, 20, 30)) {
            result = getString();
        } else {
            log.info("-------------------------没有拿到琐，退出程序------------------------------------");
            return "没有拿到琐，退出程序";
        }
        log.info(Thread.currentThread().getName() + ", result: " + result);
        return result;
    }


    /**
     * 模拟秒杀抢购,lock用法
     *
     * @return 、
     */
    @RequestMapping("/lock")
    public String lock() {
        String result = "fail";
        RLock lock = redissonUtils.lock(LOCK_KEY, 30);
        // 在琐未释放前，当前进程会一直进行等待，直到拿到琐为止
        if (lock.isLocked()) {
            result = getString();
        } else {
            log.info("-------------------------没有拿到琐，退出程序------------------------------------");
        }
        log.info(Thread.currentThread().getName() + ", result: " + result);
        return result;
    }

    @NotNull
    private String getString() {
        String result;
        try {
            log.info("-------------------------拿到琐，执行具体的业务逻辑------------------------------------");
            Integer stock = redisUtils.get(ITEM_COUNT);
            if (null != stock && stock > 0) {
                result = "success";
                redisUtils.decrement(ITEM_COUNT);
                log.info("-------------------------------秒杀成功-------------------------------");
            } else {
                log.info("-------------------------------秒杀失败，库存不足-------------------------------");
                result = "fail";
            }
        } finally {
            // 手动释放锁
            redissonUtils.unlock(LOCK_KEY);
        }
        return result;
    }
}
