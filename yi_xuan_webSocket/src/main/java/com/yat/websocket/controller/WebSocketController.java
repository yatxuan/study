package com.yat.websocket.controller;

import com.yat.websocket.WebSocketJavaClient;
import com.yat.websocket.server.WebSocketSetServer;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>Description: WebSocket客户端 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/29
 * @Time: 15:41
 */
@RestController
@RequestMapping("/web/client")
public class WebSocketController {

    @Autowired
    private WebSocketJavaClient webSocketJavaClient;

    /**
     * 服务端连接WebSocket，并发送消息
     *
     * @param sid 客户端id
     */
    @GetMapping("/{sid}")
    public void sendWebsocket(@PathVariable("sid") String sid) throws InterruptedException {
        WebSocketClient client = this.webSocketJavaClient.getClient(
                "ws://localhost:8080/websocket/" + sid
        );

        if (client != null) {
            String str = "我是客户端：" + sid;
            // 发送消息
            client.send(str);
            // 主动关闭连接
            client.closeBlocking();
        }
    }

    /**
     * 服务端直接通过WebSocket，发送消息
     *
     * @param sid     指定收信人的客户端id
     * @param message 、
     * @return 、
     */
    @RequestMapping("/socket/push/{sid}")
    public String pushToWeb(@PathVariable String sid, @RequestBody String message) {
        try {
            WebSocketSetServer.sendInfo(message, sid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

}
