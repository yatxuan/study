package com.yat.common.constant;

/**
 * <p>Description: redis - Key 值常量 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/24
 * @Time: 13:39
 */
public interface RedisConstant {

    /**
     * 存储用户的权限
     */
    String USER_PER = "User:Per:";
    /**
     * 存储用户的权限
     */
    String USER_ROLE = "User:Role:";
    /**
     * 存储用户信息，不包含权限
     */
    String USER_INFO = "User:Info:";
    /**
     * 存储用户的前端菜单
     */
    String USER_MENU = "User:Menu:";

    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    String ONLINE_KEY = "ONLINE:TOKEN:";
    /**
     * 验证码 key
     */
    String CODE_KEY = "CODE:KEY:";
}
