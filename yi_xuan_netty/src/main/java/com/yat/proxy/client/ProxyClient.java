package com.yat.proxy.client;

import com.yat.properties.NettyProxyClientProperties;
import com.yat.proxy.client.listener.ChannelStatusListener;
import com.yat.proxy.handler.ClientChannelHandler;
import com.yat.proxy.handler.RealServerChannelHandler;
import com.yat.proxy.protocol.IdleCheckHandler;
import com.yat.proxy.protocol.ProxyMessage;
import com.yat.proxy.protocol.ProxyMessageDecoder;
import com.yat.proxy.protocol.ProxyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/6 14:42
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({NettyProxyClientProperties.class})
@ConditionalOnProperty(
        value = {"netty.proxy-client.enabled"},
        matchIfMissing = false
)
public class ProxyClient implements ChannelStatusListener {

    @Autowired
    NettyProxyClientProperties nettyProxyClientProperties;

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    private static final int LENGTH_ADJUSTMENT = 0;

    private NioEventLoopGroup workerGroup;

    private Bootstrap bootstrap;

    private Bootstrap realServerBootstrap;


    private long sleepTimeMill = 1000;

    @Bean("startProxyClient")
    public String start() {
        Thread thread = new Thread(() -> {
            workerGroup = new NioEventLoopGroup();
            realServerBootstrap = new Bootstrap();
            realServerBootstrap.group(workerGroup);
            realServerBootstrap.channel(NioSocketChannel.class);
            realServerBootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new RealServerChannelHandler());
                }
            });
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProxyMessageDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                    ch.pipeline().addLast(new ProxyMessageEncoder());
                    ch.pipeline().addLast(new IdleCheckHandler(IdleCheckHandler.READ_IDLE_TIME, IdleCheckHandler.WRITE_IDLE_TIME - 10, 0));
                    ch.pipeline().addLast(new ClientChannelHandler(realServerBootstrap, bootstrap, ProxyClient.this, nettyProxyClientProperties.getProxyKeys()));
                }
            });
            connectProxyServer();
        });
        thread.setName("proxy-client");
        thread.start();
        return "";
    }


    private ChannelHandler createSslHandler(SSLContext sslContext) {
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
        return new SslHandler(sslEngine);
    }

    private void connectProxyServer() {
        bootstrap.connect(nettyProxyClientProperties.getProxyHost(), nettyProxyClientProperties.getProxyPort()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {

                    // 连接成功，向服务器发送客户端认证信息（clientKey）
                    ClientChannelMannager.setCmdChannel(future.channel());
                    ProxyMessage proxyMessage = new ProxyMessage();
                    proxyMessage.setType(ProxyMessage.C_TYPE_AUTH);
                    proxyMessage.setUri(nettyProxyClientProperties.getProxyKeys());
                    future.channel().writeAndFlush(proxyMessage);
                    sleepTimeMill = 1000;
                    log.info("connect proxy server success, {}", future.channel());
                } else {
                    log.warn("connect proxy server failed", future.cause());

                    // 连接失败，发起重连
                    reconnectWait();
                    connectProxyServer();
                }
            }
        });
    }

    public void stop() {
        workerGroup.shutdownGracefully();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        reconnectWait();
        connectProxyServer();
    }

    private void reconnectWait() {
        try {
            if (sleepTimeMill > 60000) {
                sleepTimeMill = 1000;
            }

            synchronized (this) {
                sleepTimeMill = sleepTimeMill * 2;
                wait(sleepTimeMill);
            }
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        new ProxyClient().start();
        //ContainerHelper.start(Arrays.asList(new Container[] { new ProxyClientContainer() }));
    }

}
