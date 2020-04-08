package com.yat.social.oauth.cache;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description: 缓存调度器 </p>
 * @author Yat-Xuan
 * @date 2020/4/8 17:18
*/
public enum AuthCacheScheduler {

    /**
     * 当前实例
     */
    INSTANCE;

    private AtomicInteger cacheTaskNumber = new AtomicInteger(1);
    private ScheduledExecutorService scheduler;

    AuthCacheScheduler() {
        create();
    }

    private void create() {
        this.shutdown();
        this.scheduler = new ScheduledThreadPoolExecutor(10, r -> new Thread(r, String.format("YatXuan-Task-%s", cacheTaskNumber.getAndIncrement())));
    }

    private void shutdown() {
        if (null != scheduler) {
            this.scheduler.shutdown();
        }
    }

    public void schedule(Runnable task, long delay) {
        this.scheduler.scheduleAtFixedRate(task, delay, delay, TimeUnit.MILLISECONDS);
    }
}
