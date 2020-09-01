package com.yat.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: RedissonClient工具类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/23 16:08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonUtils {

    private final RedissonClient redissonClient;

    /**
     * 默认超时时间 5秒
     */
    private static final int EXPIRE_TIME = 5;

    /**
     * 默认时间类型 秒
     */
    private static final TimeUnit TIME_TYPE = TimeUnit.SECONDS;

    /**
     * Redisson是用于连接Redis Server的基础类
     *
     * @return 、 、
     */
    public RedissonClient getRedisson() {
        return redissonClient;
    }

    /**
     * 关闭Redisson客户端连接
     *
     * @param redisson 、
     */
    public void closeRedisson(RedissonClient redisson) {
        redisson.shutdown();
        log.info("成功关闭Redis Client连接");
    }

    /**
     * 获取字符串对象
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public <T> RBucket<T> getRBucket(RedissonClient redisson, String objectName) {
        return redisson.getBucket(objectName);
    }

    /**
     * 获取Map对象
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public <K, V> RMap<K, V> getRMap(RedissonClient redisson, String objectName) {
        return redisson.getMap(objectName);
    }

    /**
     * 获取有序集合
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public <V> RSortedSet<V> getRSortedSet(RedissonClient redisson, String objectName) {
        return redisson.getSortedSet(objectName);
    }

    /**
     * 获取集合
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public <V> RSet<V> getRSet(RedissonClient redisson, String objectName) {
        return redisson.getSet(objectName);
    }

    /**
     * 获取列表
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public <V> RList<V> getRList(RedissonClient redisson, String objectName) {
        return redisson.getList(objectName);
    }

    /**
     * 获取队列
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public <V> RQueue<V> getRQueue(RedissonClient redisson, String objectName) {
        return redisson.getQueue(objectName);
    }

    /**
     * 获取双端队列
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public <V> RDeque<V> getRDeque(RedissonClient redisson, String objectName) {
        return redisson.getDeque(objectName);
    }

    /**
     * 此方法不可用在Redisson 1.2 中
     * 在1.2.2版本中可用
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public <V> RBlockingQueue<V> getRBlockingQueue(RedissonClient redisson, String objectName) {
        return redisson.getBlockingQueue(objectName);
    }

    /**
     * 获取原子数
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public RAtomicLong getRAtomicLong(RedissonClient redisson, String objectName) {
        return redisson.getAtomicLong(objectName);
    }

    /**
     * 获取记数锁
     *
     * @param redisson   、
     * @param objectName 、
     * @return 、
     */
    public RCountDownLatch getRCountDownLatch(RedissonClient redisson, String objectName) {
        return redisson.getCountDownLatch(objectName);
    }

    /**
     * 获取消息的Topic
     *
     * @param redisson   、   、
     * @param objectName 、 、
     * @return 、 、
     */
    public RTopic getRTopic(RedissonClient redisson, String objectName) {
        return redisson.getTopic(objectName);
    }


    /**
     * 获取注解类型
     *
     * @param annotationClass 、
     * @param annotations     、
     * @param <T>             、
     * @return 、
     */
    private <T extends Annotation> T getAnnotation(final Class<T> annotationClass, final Annotation[] annotations) {
        if (annotations != null && annotations.length > 0) {
            for (final Annotation annotation : annotations) {
                if (annotationClass.equals(annotation.annotationType())) {
                    return (T) annotation;
                }
            }
        }
        return null;
    }

    //---------------------------------------重入锁（Reentrant Lock）---------------------------------------------------------------

    /**
     * TODO：加锁 lock() 方法尝试获取锁，如果成功获得锁，则继续往下执行，
     * 否则等待锁被释放，然后再继续尝试获取锁，直到成功获得锁。
     * 拿不到lock就不罢休，不然线程就一直block
     *
     * @param lockKey 锁的名称
     * @return 、
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(EXPIRE_TIME, TIME_TYPE);
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey 锁的名称
     * @param timeout 超时时间   单位：秒
     */
    public RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TIME_TYPE);
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey key
     * @param unit    时间单位
     * @param timeout 超时时间
     */
    public RLock lock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * TODO：尝试获取锁 马上返回，拿到lock就返回true，不然返回false。
     * 该方法的使用需要注意（小心）
     * 在业务逻辑做操作 if(tryLock(lockKey, waitTime, leaseTime)){执行具体逻辑}else{不再执行}
     *
     * @param lockKey   key
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return 、
     */

    public boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TIME_TYPE);
        } catch (Exception e) {
            log.error("分布式锁尝试获取失败tryLock：{}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 尝试获取锁 该方法的使用需要注意（小心）
     * 在业务逻辑做操作 if(tryLock(lockKey, waitTime, leaseTime)){执行具体逻辑}else{不再执行}
     *
     * @param lockKey   、
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return 、
     */

    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (Exception e) {
            log.error("分布式锁尝试获取失败tryLock:{}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁的名称
     */

    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            unlock(lock);
        }
    }

    /**
     * 释放锁
     *
     * @param lock 、
     */

    private void unlock(RLock lock) {
        lock.unlock();
    }


    //--------------------------------以下不建议使用-除非理解透彻--------------------------------------------------

    /**
     * 分布式锁的异步执行 解锁 unlockAsync(key)
     * 在业务逻辑做操作 if(asyncReentrantLock(lockKey, waitTime, leaseTime)){执行具体逻辑}else{不再执行}
     *
     * @param lockKey   、
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     */
    public Boolean asyncReentrantLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        Boolean res = false;
        try {
            Future<Boolean> result = lock.tryLockAsync(waitTime, leaseTime, TIME_TYPE);
            res = result.get();
        } catch (Exception e) {
            log.error("分布式锁异步执行失败asyncReentrantLock:{}", ExceptionUtils.getStackTrace(e));
        }
        return res;
    }

    /**
     * 公平锁
     * 当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     *
     * @param lockKey
     * @param leaseTime
     */
    public boolean fairLock(String lockKey, int waitTime, int leaseTime) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        try {
            // 该方法需要 判断锁的持有情况
            return fairLock.tryLock(waitTime, leaseTime, TIME_TYPE);
        } catch (Exception e) {
            log.error("分布式锁公平锁失败asyncReentrantLock:{}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 公平锁
     * 当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     *
     * @param lockKey
     * @param leaseTime
     */
    public void fairLock(String lockKey, int leaseTime) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        // 该方法没有返回值，一定会加锁
        fairLock.lock(leaseTime, TIME_TYPE);
    }

    /**
     * 公平锁 的异步执行
     * 保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     *
     * @param lockKey   、
     * @param leaseTime 、
     */
    public boolean fairAsyncLock(String lockKey, int waitTime, int leaseTime) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        Boolean res = false;
        try {
            Future<Boolean> result = fairLock.tryLockAsync(waitTime, leaseTime, TIME_TYPE);
            res = result.get();
        } catch (Exception e) {
            log.error("公平锁 的异步执行失败asyncReentrantLock:{}", ExceptionUtils.getStackTrace(e));
        }
        return res;
    }

}
