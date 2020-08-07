package com.yat.proxy.server;

import com.yat.properties.NettyProxyServerProperties;
import com.yat.proxy.handler.ServerChannelHandler;
import com.yat.proxy.handler.UserChannelHandler;
import com.yat.proxy.protocol.IdleCheckHandler;
import com.yat.proxy.protocol.ProxyMessageDecoder;
import com.yat.proxy.protocol.ProxyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.BindException;
import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/6 14:49
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = {"netty.proxy-server.enabled"},
        matchIfMissing = false
)
@EnableConfigurationProperties({NettyProxyServerProperties.class})
public class ProxyServer {

    private final NettyProxyServerProperties nettyProxyServerProperties;
    private final UserChannelHandler userChannelHandler;
    private final ServerChannelHandler serverChannelHandler;

    /**
     * max packet is 2M.
     */
    private static final int MAX_FRAME_LENGTH = 2 * 1024 * 1024;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    private static final int LENGTH_ADJUSTMENT = 0;

    @Bean("startProxyServer")
    public String start() {
        //    new ProxyConfig( nettyProxyServerProperties);
        Thread thread = new Thread(() -> {
            ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup serverBossGroup = new NioEventLoopGroup();
            NioEventLoopGroup serverWorkerGroup = new NioEventLoopGroup();
            bootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProxyMessageDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                    ch.pipeline().addLast(new ProxyMessageEncoder());
                    ch.pipeline().addLast(new IdleCheckHandler(IdleCheckHandler.READ_IDLE_TIME, IdleCheckHandler.WRITE_IDLE_TIME, 0));
                    ch.pipeline().addLast(serverChannelHandler);
                }
            });
            try {
                log.info("start netty [Proxy Server] server ,port:" + nettyProxyServerProperties.getPort());
                bootstrap.bind(nettyProxyServerProperties.getPort()).get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            startUserPort();
        });
        thread.setName("Proxy_Server");
        thread.start();
        return "";
    }


    private void startUserPort() {
        Thread thread = new Thread(() -> {
            ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup serverBossGroup = new NioEventLoopGroup();
            NioEventLoopGroup serverWorkerGroup = new NioEventLoopGroup();
            bootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    //  ch.pipeline().addFirst(new BytesMetricsHandler());
                    ch.pipeline().addLast(userChannelHandler);
                }
            });
            //  List<Integer> ports = ProxyConfig.getInstance().getUserPorts();
            List<Integer> ports = nettyProxyServerProperties.ports();
            for (int port : ports) {
                try {
                    bootstrap.bind(port).get();
                    log.info("bind user port " + port);
                } catch (Exception ex) {
                    // BindException表示该端口已经绑定过
                    if (!(ex.getCause() instanceof BindException)) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        thread.setName("UseSever_Server");
        thread.start();

    }


}
