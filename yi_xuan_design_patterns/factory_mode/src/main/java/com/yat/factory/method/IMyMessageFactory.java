package com.yat.factory.method;

import com.yat.factory.method.message.IMyMessage;

/**
 * <p>Description: 工厂方法模式_工厂接口 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/25 16:22
 */
public interface IMyMessageFactory {

    /**
     * 获取产品类型
     *
     * @param messageType 、
     * @return 、
     */
    IMyMessage createMessage(String messageType);
}
