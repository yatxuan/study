package com.yat.config.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static com.yat.config.socket.SocketPool.add;
import static com.yat.config.socket.SocketPool.remove;

/**
 * <p>Description: Socket操作处理类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 9:37
 */
@Slf4j
public class SocketHandler{

    /**
     * 将连接的Socket注册到Socket池中
     * @param socket 、
     * @return 、
     */
    static ClientSocket register(Socket socket){
        ClientSocket clientSocket = new ClientSocket();
        clientSocket.setSocket(socket);
        try {
            clientSocket.setInputStream(new DataInputStream(socket.getInputStream()));
            clientSocket.setOutputStream(new DataOutputStream(socket.getOutputStream()));
            byte[] bytes = new byte[1024];
            clientSocket.getInputStream().read(bytes);
            clientSocket.setKey(new String(bytes, StandardCharsets.UTF_8));
            add(clientSocket);
            return clientSocket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向指定客户端发送信息
     * @param clientSocket 、
     * @param message 、
     */
    static void sendMessage(ClientSocket clientSocket, String message){
        try {
            clientSocket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
            // clientSocket.getOutputStream().writeUTF(message);
        } catch (IOException e) {
            log.error("发送信息异常：{}", e.getMessage());
            close(clientSocket);
        }
    }

    /**
     * 获取指定客户端的上传信息
     * @param clientSocket 、
     * @return 、
     */
    public static String onMessage(ClientSocket clientSocket){
        byte[] bytes = new byte[1024];
        try {
            clientSocket.getInputStream().read(bytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            close(clientSocket);
        }
        return null;
    }

    /**
     * 指定Socket资源回收
     * @param clientSocket 、
     */
    static void close(ClientSocket clientSocket){
        log.info("进行资源回收");
        if (clientSocket != null){
            log.info("开始回收socket相关资源，其Key为{}", clientSocket.getKey());
            remove(clientSocket.getKey());
            Socket socket = clientSocket.getSocket();
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
            } catch (IOException e) {
                log.error("关闭输入输出流异常，{}", e.getMessage());
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("关闭socket异常{}", e.getMessage());
                }
            }
        }
    }


    /**
     * 发送数据包，判断数据连接状态
     * @param clientSocket 、
     * @return 、
     */
    static boolean isSocketClosed(ClientSocket clientSocket){
        try {
            clientSocket.getSocket().sendUrgentData(1);
            return false;
        } catch (IOException e) {
            return true;
        }
    }

}
