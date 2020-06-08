package com.yat.limit.service.impl;

import com.yat.limit.service.IRosterService;
import com.yat.limit.common.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * <p>Description: ip名单接口处理，为了方便，当前ip是存放在redis进行测试，正式开发，还是放在数据库比较保险 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 9:52
 */
@Service
@RequiredArgsConstructor
public class RosterServiceImpl implements IRosterService {

    private final RedisUtils<String> redisUtils;

    /**
     * ip黑名单key
     */
    private final static String ROSTER_IP_BLACK = "ROSTER:IP:BLACK";
    /**
     * ip白名单key
     */
    private final static String ROSTER_IP_WHITE = "ROSTER:IP:WHITE";

    @Override
    public Set<String> getBlackList() {
        return redisUtils.sGet(ROSTER_IP_BLACK);
    }

    @Override
    public void setBlackList(String ip) {
        redisUtils.setAdd(ROSTER_IP_BLACK, ip);
    }

    @Override
    public boolean isBlackPresence(String ip) {
        return redisUtils.sHasKey(ROSTER_IP_BLACK, ip);
    }

    @Override
    public Set<String> getWhiteList() {
        return redisUtils.sGet(ROSTER_IP_WHITE);
    }

    @Override
    public void setWhiteList(String ip) {
        redisUtils.setAdd(ROSTER_IP_WHITE, ip);
    }

    @Override
    public boolean isWhitePresence(String ip) {
        return redisUtils.sHasKey(ROSTER_IP_BLACK, ip);
    }
}
