package com.yat.proxy.client.listener;

import io.netty.channel.Channel;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:29
 */
public interface ProxyChannelBorrowListener {

    /**
     * <p>Description: 描述 </p>
     *
     * @param channel 、
     * @author Yat-Xuan
     * @date 2020/9/17 11:31
     */
    void success(Channel channel);

    /**
     * <p>Description: 描述 </p>
     *
     * @param cause 、
     * @author Yat-Xuan
     * @date 2020/9/17 11:31
     */
    void error(Throwable cause);

}
