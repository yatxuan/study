package com.yat.netty.pipeline;

import com.yat.netty.handle.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/6 14:05
 */
@Component
@ConditionalOnProperty(  // 配置文件属性是否为true
        value = {"netty.http.enabled"},
        matchIfMissing = false
)
public class HttpPipeline extends ChannelInitializer<SocketChannel> {

    @Autowired
    HttpServerHandler httpServerHandler;


    @Override
    public void initChannel(SocketChannel ch) {
        //	log.error("test", this);
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpContentCompressor());
        p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new ChunkedWriteHandler());
        // http请求根处理器
        p.addLast(httpServerHandler);
    }

}
