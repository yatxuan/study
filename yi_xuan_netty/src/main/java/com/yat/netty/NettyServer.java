package com.yat.netty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/29 - 11:19
 */
@Component
public class NettyServer {

    @Value("${server.port}")
    private int port;


}
