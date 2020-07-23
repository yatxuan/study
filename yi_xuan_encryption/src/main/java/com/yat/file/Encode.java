package com.yat.file;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * <p>Description: 文件加密解密 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/9 14:56
 */
public class Encode {

    /**
     * 默认秘钥
     */
    private static final String DEFAULT_KEY = "DEFAULT_KEY";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final long SLEEP_TIME = 100;

    boolean isEncode;
    MappedByteBuffer mappedBuffer;

    private String filename;
    private byte[] keyBytes;

    /**
     *
     * @param isEncode true - 加密  false - 解密
     * @param filename 文件路径
     * @param key 秘钥
     */
    public Encode(boolean isEncode, String filename, String key) {
        this.isEncode = isEncode;
        this.filename = filename;

        if (StringUtils.isBlank(key)) {
            key = DEFAULT_KEY;
        }

        this.keyBytes = key.getBytes(Charset.forName(DEFAULT_CHARSET));
    }

    public void run() {
        RandomAccessFile raf = null;
        FileChannel channel = null;
        try {
            // 读取文件
            raf = new RandomAccessFile(filename, "rw");
            channel = raf.getChannel();
            long fileLength = channel.size();
            int bufferCount = (int) Math.ceil((double) fileLength / (double) Integer.MAX_VALUE);
            if (bufferCount == 0) {
                channel.close();
                raf.close();
                return;
            }
            int bufferIndex = 0;
            long preLength = 0;
            // repeat part
            long regionSize = Integer.MAX_VALUE;
            if (fileLength - preLength < Integer.MAX_VALUE) {
                regionSize = fileLength - preLength;
            }
            mappedBuffer = channel.map(FileChannel.MapMode.READ_WRITE, preLength, regionSize);
            preLength += regionSize;

            // 创建工作线程
            int threadCount = keyBytes.length;

            System.out.println(
                    "File size: " + fileLength + ", buffer count: " + bufferCount + ", thread count: " + threadCount);
            long startTime = System.currentTimeMillis();
            System.out.println("Start time: " + startTime + "ms");
            System.out.println("Buffer " + bufferIndex + " start ...");

            EncodeThread[] workThreads = new EncodeThread[threadCount];
            for (int i = 0; i < threadCount; i++) {
                workThreads[i] = new EncodeThread(this, keyBytes[i], keyBytes.length, i);
                workThreads[i].start();
            }

            // loop
            while (true) {
                Thread.sleep(SLEEP_TIME);

                // 等到所有线程完成
                boolean completed = true;
                for (EncodeThread workThread : workThreads) {
                    if (!workThread.isCompleted()) {
                        completed = false;
                        break;
                    }
                }
                if (!completed) {
                    continue;
                }

                // 检查是否完成
                bufferIndex++;
                if (bufferIndex >= bufferCount) {
                    // 停止线程
                    for (EncodeThread workThread : workThreads) {
                        workThread.flag = false;
                    }
                    break;
                }

                // 重复部分
                regionSize = Integer.MAX_VALUE;
                if (fileLength - preLength < Integer.MAX_VALUE) {
                    regionSize = fileLength - preLength;
                }
                mappedBuffer = channel.map(FileChannel.MapMode.READ_WRITE, preLength, regionSize);
                preLength += regionSize;

                // 重新启动线程
                System.out.println("Buffer " + bufferIndex + " start ...");
                for (EncodeThread workThread : workThreads) {
                    workThread.restart();
                }
            }

            // over loop
            while (true) {
                Thread.sleep(SLEEP_TIME);
                boolean isOver = true;
                for (EncodeThread workThread : workThreads) {
                    if (workThread.isAlive()) {
                        isOver = false;
                        break;
                    }
                }
                if (isOver) {
                    break;
                }
            }


            long endTime = System.currentTimeMillis();
            System.out.println("End time: " + endTime + "ms, use time: " + (endTime - startTime) + "ms");
            System.out.println("ok!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // close file relatives
            try {
                if (channel != null) {
                    channel.close();
                }
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
//
// class EncodeThread extends Thread {
//
//     private static final long SLEEP_TIME = 50;
//
//     boolean flag;
//     private Encode encoder;
//     private int key;
//     private long dataIndex;
//     private int interval;
//     private int regionSize;
//     private boolean completed;
//
//     EncodeThread(Encode encoder, byte key, int interval, int index) {
//         this.encoder = encoder;
//         this.key = key & 0xff;
//         this.dataIndex = index;
//         this.interval = interval;
//         this.regionSize = encoder.mappedBuffer.limit();
//         this.completed = false;
//         this.flag = true;
//     }
//
//     void restart() {
//         this.dataIndex -= regionSize;
//         regionSize = encoder.mappedBuffer.limit();
//         completed = false;
//     }
//
//     boolean isCompleted() {
//         return completed;
//     }
//
//     @Override
//     public void run() {
//         try {
//             if (encoder.isEncode) {
//                 encode();
//             } else {
//                 decode();
//             }
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//     }
//
//     /**
//      * 加密
//      *
//      * @throws InterruptedException 、
//      */
//     private void encode() throws InterruptedException {
//         while (flag) {
//             if (completed) {
//                 Thread.sleep(SLEEP_TIME);
//                 continue;
//             }
//             if (dataIndex >= regionSize) {
//                 completed = true;
//                 System.out.println("encode: Encode thread " + this.getName() + " is completed!");
//                 continue;
//             }
//
//             byte b = encoder.mappedBuffer.get((int) dataIndex);
//             b = (byte) (((b & 0xff) + key) % 256);
//             encoder.mappedBuffer.put((int) dataIndex, b);
//             dataIndex += interval;
//         }
//     }
//
//     /**
//      * 解密
//      *
//      * @throws InterruptedException 、
//      */
//     private void decode() throws InterruptedException {
//         while (flag) {
//             if (completed) {
//                 Thread.sleep(SLEEP_TIME);
//                 continue;
//             }
//             if (dataIndex >= regionSize) {
//                 completed = true;
//                 System.out.println("decode:Encode thread " + this.getName() + " is completed!");
//                 continue;
//             }
//
//             byte b = encoder.mappedBuffer.get((int) dataIndex);
//             b = (byte) (((b & 0xff) + 256 - key) % 256);
//             encoder.mappedBuffer.put((int) dataIndex, b);
//             dataIndex += interval;
//         }
//     }
//
// }
