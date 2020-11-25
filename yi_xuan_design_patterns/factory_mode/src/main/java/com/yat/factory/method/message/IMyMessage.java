package com.yat.factory.method.message;

import java.util.Map;

/**
 * <p>Description: 工厂方法模式_产品接口 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/25 16:22
 */
public interface IMyMessage {

    Map<String, Object> getMessageParam();

    /**
     * 发送消息
     * @param messageParam \
     */
    void setMessageParam(Map<String, Object> messageParam);

    /**
     * 发送通知/消息
     */
    void sendMessage() throws Exception;

}
