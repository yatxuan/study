package com.yat.proxy.client.listener;

import io.netty.channel.ChannelHandlerContext;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:29
 */
public interface ChannelStatusListener {

    /**
     * 、
     * @param ctx 、
     */
    void channelInactive(ChannelHandlerContext ctx);

}
