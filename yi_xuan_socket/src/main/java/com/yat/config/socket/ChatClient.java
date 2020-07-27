package com.yat.config.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * <p>Description: 客户端 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/27 14:21
 */
@Slf4j
public class ChatClient {

    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 8068;

        //与服务端建立连接
        Socket socket = new Socket(host, port);
        socket.setOOBInline(true);

        //建立连接后获取输出流
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        String uuid = UUID.randomUUID().toString();
        log.info("uuid: {}", uuid);
        outputStream.write(uuid.getBytes());
        String content = "";
        while (true) {
            byte[] buff = new byte[1024];
            int read = inputStream.read(buff);
            log.info("read:{}", read);
            String buffer = new String(buff, StandardCharsets.UTF_8);
            content += buffer;
            log.info("info: {}", content);
            // 把内容写入文件
            File file = new File("D:\\json.txt");
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    log.info("文件创建成功");
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.flush();

            fileWriter.close();
            bufferedWriter.close();
        }
    }
}
