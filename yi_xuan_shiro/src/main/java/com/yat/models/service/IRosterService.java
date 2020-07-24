package com.yat.models.service;

import java.util.Set;

/**
 * <p>Description: ip黑名单、白名单接口 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 17:50
 */
public interface IRosterService {

    /**
     * 获取当前所有黑名单数据
     *
     * @return 返回ip黑名单
     */
    Set<String> getBlackList();

    /**
     * 存储ip黑名单数据
     *
     * @param ip IP地址
     */
    void setBlackList(String ip);

    /**
     * 判断当前ip，是否存在与黑名单中
     *
     * @param ip IP地址
     * @return true-存在，false-不存在
     */
    boolean isBlackPresence(String ip);

    /**
     * 获取当前所有白名单数据
     *
     * @return 返回ip白名单
     */
    Set<String> getWhiteList();

    /**
     * 存储ip白名单数据
     *
     * @param ip IP地址
     */
    void setWhiteList(String ip);

    /**
     * 判断当前ip，是否存在于白名单中
     *
     * @param ip IP地址
     * @return true-存在，false-不存在
     */
    boolean isWhitePresence(String ip);
}