package com.yat.websocket.entity;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/9 11:44
 */
public enum MsgType {
    /**
     * 连接
     */
    CONNECT,
    /**
     * 关闭
     */
    CLOSE,
    /**
     * 信息
     */
    INFO,
    /**
     * 错误
     */
    ERROR
}
