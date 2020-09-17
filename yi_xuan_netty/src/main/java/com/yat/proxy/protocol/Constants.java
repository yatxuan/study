package com.yat.proxy.protocol;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:35
 */
public interface Constants {

    public static final AttributeKey<Channel> NEXT_CHANNEL = AttributeKey.newInstance("nxt_channel");

    public static final AttributeKey<String> USER_ID = AttributeKey.newInstance("user_id");

    public static final AttributeKey<String> CLIENT_KEY = AttributeKey.newInstance("client_key");
}
