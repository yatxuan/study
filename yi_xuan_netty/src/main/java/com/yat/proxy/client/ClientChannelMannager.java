package com.yat.proxy.client;

import com.yat.proxy.client.listener.ProxyChannelBorrowListener;
import com.yat.proxy.protocol.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 代理客户端与后端真实服务器连接管理
 *
 * @author fengfei
 */
@Slf4j
public class ClientChannelMannager {

    private static final AttributeKey<Boolean> USER_CHANNEL_WRITEABLE = AttributeKey.newInstance("user_channel_writeable");

    private static final AttributeKey<Boolean> CLIENT_CHANNEL_WRITEABLE = AttributeKey.newInstance("client_channel_writeable");

    private static final int MAX_POOL_SIZE = 100;

    private static Map<String, Channel> realServerChannels = new ConcurrentHashMap<>(16);

    private static ConcurrentLinkedQueue<Channel> proxyChannelPool = new ConcurrentLinkedQueue<>();

    private static volatile Channel cmdChannel;

    public static void borrowProxyChanel(Integer port, Bootstrap bootstrap, final ProxyChannelBorrowListener borrowListener) {
        Channel channel = proxyChannelPool.poll();
        if (channel != null) {
            borrowListener.success(channel);
            return;
        }

        bootstrap.connect("", port)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        borrowListener.success(future.channel());
                    } else {
                        log.warn("connect proxy server failed", future.cause());
                        borrowListener.error(future.cause());
                    }
                });
    }


    public static void returnProxyChanel(Channel proxyChanel) {
        if (proxyChannelPool.size() > MAX_POOL_SIZE) {
            proxyChanel.close();
        } else {
            proxyChanel.config().setOption(ChannelOption.AUTO_READ, true);
            proxyChanel.attr(Constants.NEXT_CHANNEL).remove();
            proxyChannelPool.offer(proxyChanel);
            log.debug("return ProxyChanel to the pool, channel is {}, pool size is {} ", proxyChanel, proxyChannelPool.size());
        }
    }

    public static void removeProxyChanel(Channel proxyChanel) {
        proxyChannelPool.remove(proxyChanel);
    }

    public static void setCmdChannel(Channel cmdChannel) {
        ClientChannelMannager.cmdChannel = cmdChannel;
    }

    public static Channel getCmdChannel() {
        return cmdChannel;
    }

    public static void setRealServerChannelUserId(Channel realServerChannel, String userId) {
        realServerChannel.attr(Constants.USER_ID).set(userId);
    }

    public static String getRealServerChannelUserId(Channel realServerChannel) {
        return realServerChannel.attr(Constants.USER_ID).get();
    }

    public static Channel getRealServerChannel(String userId) {
        return realServerChannels.get(userId);
    }

    public static void addRealServerChannel(String userId, Channel realServerChannel) {
        realServerChannels.put(userId, realServerChannel);
    }

    public static Channel removeRealServerChannel(String userId) {
        return realServerChannels.remove(userId);
    }

    public static boolean isRealServerReadable(Channel realServerChannel) {
        return realServerChannel.attr(CLIENT_CHANNEL_WRITEABLE).get() && realServerChannel.attr(USER_CHANNEL_WRITEABLE).get();
    }

    public static void clearRealServerChannels() {
        log.warn("channel closed, clear real server channels");
        for (Entry<String, Channel> stringChannelEntry : realServerChannels.entrySet()) {
            Channel realServerChannel = stringChannelEntry.getValue();
            if (realServerChannel.isActive()) {
                realServerChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }

        realServerChannels.clear();
    }
}
