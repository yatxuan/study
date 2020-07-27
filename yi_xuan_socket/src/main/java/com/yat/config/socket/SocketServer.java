package com.yat.config.socket;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.yat.config.socket.SocketHandler.register;

/**
 * <p>Description: 服务端 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/27 11:10
 */
@Slf4j
@Data
@Component
@PropertySource("classpath:socket.properties")
@NoArgsConstructor
public class SocketServer {

    @Value("${port}")
    private Integer port;
    private boolean started;
    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        new SocketServer().start(8068);
    }

    public void start() {
        start(null);
    }

    public void start(Integer port) {
        log.info("port: {}, {}", this.port, port);
        try {
            serverSocket = new ServerSocket(port == null ? this.port : port);
            started = true;
            log.info("Socket服务已启动，占用端口： {}", serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("端口冲突,异常信息：{}", e.getMessage());
            System.exit(0);
        }

        while (started) {
            try {
                Socket socket = serverSocket.accept();
                socket.setKeepAlive(true);
                ClientSocket register = register(socket);
                if (register != null) {
                    log.info("客户端已连接，其Key值为：{}", register.getKey());
                    String json = "{\n" +
                            "\"status\": 200,\n" +
                            "\"timestamp\": \"2020-07-27 10:26:32\",\n" +
                            "\"message\": \"接收成功\"\n" +
                            "}";
                    SocketHandler.sendMessage(register, json);
                    executorService.submit(register);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
