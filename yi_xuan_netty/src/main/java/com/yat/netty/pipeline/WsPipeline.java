package com.yat.netty.pipeline;

import com.yat.netty.handle.WsServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/6 14:36
 */
@Component
@ConditionalOnProperty(  //配置文件属性是否为true
        value = {"netty.ws.enabled"},
        matchIfMissing = false
)
@RequiredArgsConstructor
public class WsPipeline extends ChannelInitializer<SocketChannel> {

    private final WsServerHandler wsServerHandler;

    /** 读超时 */
    private static final int READ_IDEL_TIME_OUT = 3;
    /** 写超时 */
    private static final int WRITE_IDEL_TIME_OUT = 4;
    /** 所有超时 */
    private static final int ALL_IDEL_TIME_OUT = 5;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline p = ch.pipeline();
        p.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.MINUTES));
        p.addLast("http-codec", new HttpServerCodec());
        p.addLast("aggregator", new HttpObjectAggregator(65536));
        p.addLast("http-chunked", new ChunkedWriteHandler());
        p.addLast("handler", wsServerHandler);
    }

}
