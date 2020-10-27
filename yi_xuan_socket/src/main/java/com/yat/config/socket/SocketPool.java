package com.yat.config.socket;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 9:38
 */
public class SocketPool {

    private static final ConcurrentHashMap<String, ClientSocket> ONLINE_SOCKET_MAP = new ConcurrentHashMap<>();


    public static void add(ClientSocket clientSocket){
        if (clientSocket != null && !clientSocket.getKey().isEmpty()) {
            ONLINE_SOCKET_MAP.put(clientSocket.getKey(), clientSocket);
        }
    }

    public static void remove(String key){
        if (!key.isEmpty()) {
            ONLINE_SOCKET_MAP.remove(key);
        }
    }
}
