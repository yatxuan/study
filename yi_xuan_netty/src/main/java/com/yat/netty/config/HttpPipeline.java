// package com.yat.netty.config;
//
// import io.netty.channel.ChannelInitializer;
// import io.netty.channel.ChannelPipeline;
// import io.netty.channel.socket.SocketChannel;
// import io.netty.handler.codec.http.HttpContentCompressor;
// import io.netty.handler.codec.http.HttpObjectAggregator;
// import io.netty.handler.codec.http.HttpServerCodec;
// import io.netty.handler.stream.ChunkedWriteHandler;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.stereotype.Component;
//
// /**
//  * <p>Description: 描述 </p>
//  *
//  * @author Yat-Xuan
//  * @date 2020/6/2 15:06
//  */
// @Component
// @ConditionalOnProperty(value = {"netty.http.enabled"})
// public class HttpPipeline extends ChannelInitializer<SocketChannel> {
//
//     @Autowired
//     HttpServerHandler httpServerHandler;
//
//
//     @Override
//     public void initChannel(SocketChannel ch) {
//         ChannelPipeline p = ch.pipeline();
//         p.addLast(new HttpServerCodec());
//         p.addLast(new HttpContentCompressor());
//         p.addLast(new HttpObjectAggregator(1048576));
//         p.addLast(new ChunkedWriteHandler());
//         // http请求根处理器
//         p.addLast(httpServerHandler);
//     }
//
// }
