package com.yat.file;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/9 15:03
 */
public class EncodeThread extends Thread {

    private static final long SLEEP_TIME = 50;

    boolean flag;
    private Encode encoder;
    private int key;
    private long dataIndex;
    private int interval;
    private int regionSize;
    private boolean completed;

    EncodeThread(Encode encoder, byte key, int interval, int index) {
        this.encoder = encoder;
        this.key = key & 0xff;
        this.dataIndex = index;
        this.interval = interval;
        this.regionSize = encoder.mappedBuffer.limit();
        this.completed = false;
        this.flag = true;
    }

    void restart() {
        this.dataIndex -= regionSize;
        regionSize = encoder.mappedBuffer.limit();
        completed = false;
    }

    boolean isCompleted() {
        return completed;
    }

    @Override
    public void run() {
        try {
            if (encoder.isEncode) {
                encode();
            } else {
                decode();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密
     *
     * @throws InterruptedException 、
     */
    private void encode() throws InterruptedException {
        while (flag) {
            if (completed) {
                Thread.sleep(SLEEP_TIME);
                continue;
            }
            if (dataIndex >= regionSize) {
                completed = true;
                System.out.println("encode: Encode thread " + this.getName() + " is completed!");
                continue;
            }

            byte b = encoder.mappedBuffer.get((int) dataIndex);
            b = (byte) (((b & 0xff) + key) % 256);
            encoder.mappedBuffer.put((int) dataIndex, b);
            dataIndex += interval;
        }
    }

    /**
     * 解密
     *
     * @throws InterruptedException 、
     */
    private void decode() throws InterruptedException {
        while (flag) {
            if (completed) {
                Thread.sleep(SLEEP_TIME);
                continue;
            }
            if (dataIndex >= regionSize) {
                completed = true;
                System.out.println("decode:Encode thread " + this.getName() + " is completed!");
                continue;
            }

            byte b = encoder.mappedBuffer.get((int) dataIndex);
            b = (byte) (((b & 0xff) + 256 - key) % 256);
            encoder.mappedBuffer.put((int) dataIndex, b);
            dataIndex += interval;
        }
    }

}
