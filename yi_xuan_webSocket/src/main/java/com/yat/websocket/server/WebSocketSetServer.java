package com.yat.websocket.server;

import com.yat.websocket.common.util.RedisUtils;
import com.yat.websocket.common.util.SpringContextUtils;
import com.yat.websocket.config.HttpSessionConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * CopyOnWriteArraySet 存储
 *
 * @author: yat
 * @date: 2019-08-10 15:46
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/set/{sid}", configurator = HttpSessionConfig.class)
public class WebSocketSetServer {

    /**
     * 弹幕
     */
    final static String BARRAGE_REDIS = "BARRAGE:";
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketSetServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收sid
     */
    private String sid;

    private String sessionId;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid, EndpointConfig config) throws IOException {

        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        this.session = session;
        this.sid = sid;
        this.sessionId = httpSession.getId();

        // 判断当前连是否为合法连接
        isLegalConnection(session, sid);

        //如果存在就先删除一个，防止重复推送消息
        for (WebSocketSetServer webSocket : webSocketSet) {
            if (webSocket.sid.equals(sid)) {
                webSocketSet.remove(webSocket);
            }
        }
        webSocketSet.add(this);
        this.sid = sid;
        sendMessage("客户端：" + this.sid + ",已连接");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("客户端：{},已关闭连接", this.sid);
        webSocketSet.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {


        // 发言限制
        RedisUtils redisUtils = SpringContextUtils.getBean("redisUtils", RedisUtils.class);
        if (redisUtils.hasKey(BARRAGE_REDIS + this.sessionId)) {
            log.error("当前用户：‘{}’,弹幕消息发送过快", this.sessionId);
            sendMessage("当前用户：‘{" + this.sessionId + "}’,弹幕消息发送过快");
            return;
        } else {
            redisUtils.set(BARRAGE_REDIS + this.sessionId, 1, 10L);
        }

        log.info("'服务端'收到来自'客户端'：" + sid + "的信息:" + message);
        int size = webSocketSet.size();
        // 回复当前发送消息的客户端
        sendMessage("当前连接人数：---------->" + size);
        //群发消息
        sendInfo(message, null);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("'WebSocketServer:'发生错误");
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
     * @param sid       客户端id（这里可以选择推送给指定的客户端，传进来的sid为空，则推送给全部的客户端，）
     * @throws IOException 、
     */
    public static void sendInfo(String socketMsg, String sid) throws IOException {
        String str = sid == null ? "所有人" : "'客户端：'" + sid;
        log.info("'服务端'推送消息给" + str + "，推送内容为:" + socketMsg);
        for (WebSocketSetServer item : webSocketSet) {
            if (sid == null) {
                // 所有客户端都推送消息
                item.sendMessage(socketMsg);
            } else if (item.sid.equals(sid)) {
                // 只给指定的客户端推送消息
                item.sendMessage(socketMsg);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketSetServer that = (WebSocketSetServer) o;
        return Objects.equals(session, that.session) &&
                Objects.equals(sid, that.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, sid);
    }

    /**
     * 判断当前连接是否为合法连接
     *
     * @param session 、
     * @param sid     、
     */
    private void isLegalConnection(Session session, String sid) throws IOException {
        if (!StringUtils.contains(sid, "+")) {
            webSocketSet.remove(this);
            log.error("WebSocket连接时，sid参数不正确，禁止连接");
            throw new IOException("webSocket参数错误");
        }
    }
}
