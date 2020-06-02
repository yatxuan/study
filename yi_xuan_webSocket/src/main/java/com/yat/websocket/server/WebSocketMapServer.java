package com.yat.websocket.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap存储
 * @author: yat
 * @date: 2019-08-10 15:46
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketMapServer {
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketMapServer> webSocketMap = new ConcurrentHashMap<>(16);
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收sid
     */
    private String userId;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        this.session = session;
        this.userId = userId;

        // 如果存在就先删除一个，防止重复推送消息
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //加入set中
            webSocketMap.put(userId, this);
        } else {
            //加入set中
            webSocketMap.put(userId, this);
            //在线数加1
            addOnlineCount();
        }
        sendMessage("客户端：" + this.userId + ",已连接");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:'{}',当前在线人数为:'{}'", userId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("用户消息:'{}',报文:'{}'", this.userId, message);

        // 发送消息给所有人
        sendBulk("当前连接人数：---------->" + getOnlineCount());

        // 回复当前发送消息的客户端
        sendMessage("当前连接人数：---------->" + getOnlineCount());
        if (StringUtils.isNotBlank(message)) {
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId", this.userId);
                String toUserId = jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                } else {
                    log.error("请求的userId:" + toUserId + "不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     *
     * @param socketMsg 消息
     * @throws IOException 、
     */
    public static void sendBulk(String socketMsg) throws IOException {
        for (WebSocketMapServer item : webSocketMap.values()) {
            item.sendMessage(socketMsg);
        }
    }

    /**
     * 发送自定义消息
     *
     * @param message 消息
     * @param userId  收信人id
     * @throws IOException 、
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    /**
     * 获取当前在线人数
     *
     * @return 人数
     */
    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 增加在线人数
     */
    private static synchronized void addOnlineCount() {
        WebSocketMapServer.onlineCount++;
    }

    /**
     * 减少在线人数
     */
    private static synchronized void subOnlineCount() {
        WebSocketMapServer.onlineCount--;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketMapServer that = (WebSocketMapServer) o;
        return Objects.equals(session, that.session) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, userId);
    }
}
