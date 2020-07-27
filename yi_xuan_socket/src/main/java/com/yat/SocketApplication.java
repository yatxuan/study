package com.yat;

import com.yat.config.socket.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/17 - 16:04
 */
@SpringBootApplication
public class SocketApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SocketApplication.class);
        // 在spring容器启动后，取到已经初始化的SocketServer，启动Socket服务
        context.getBean(SocketServer.class).start();
        System.out.println("--------------------------------系统初始化结束-----------------------------------");
    }
}
