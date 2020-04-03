package com.yat;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>Description: 文件压缩：把文件压缩成待解压文件（zip文件） </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/3 15:15
 */
public class FileCompress {

    /**
     * 要压缩的图片文件所在所存放位置
     */
    private static String JPG_FILE_PATH = "D:\\C盘下载的文件\\智慧厅堂.mp4";

    /**
     * zip压缩包所存放的位置
     */
    private static String ZIP_FILE = "D:\\Work\\Idea/hu.zip";

    /**
     * 所要压缩的文件
     */
    private static File JPG_FILE;

    /**
     * 文件大小
     */
    private static long FILE_SIZE;

    /**
     * 文件名
     */
    private static String FILE_NAME;

    /**
     * 文件后缀名
     */
    private static String SUFFIX_FILE;

    static {
        File file = new File(JPG_FILE_PATH);
        JPG_FILE = file;
        FILE_NAME = file.getName();
        FILE_SIZE = file.length();
        SUFFIX_FILE = file.getName().substring(file.getName().indexOf('.'));
    }

    public static void main(String[] args) {

//        Options opt = new OptionsBuilder()
//                .include(FileCompress.class.getSimpleName())
//                .forks(1).warmupIterations(10).threads(1)
//                .build();
//        new Runner(opt).run();

        // System.out.println("------NoBuffer");
        // zipFileNoBuffer();

        // System.out.println("------Buffer");
        // zipFileBuffer();

        // System.out.println("------Channel");
        // zipFileChannel();

        // System.out.println("---------Map");
        // zipFileMap();

        // System.out.println("-------Pip");
        // zipFilePip();

        zipFileMapBuffer();
        // test();
    }

    /**
     * Version 1 没有使用Buffer
     * 100兆文件压缩，耗时：秒
     */
    public static void zipFileNoBuffer() {

        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            //开始时间
            long beginTime = System.currentTimeMillis();

            try (InputStream input = new FileInputStream(JPG_FILE)) {
                zipOut.putNextEntry(new ZipEntry(FILE_NAME + 0));
                int temp;
                while ((temp = input.read()) != -1) {
                    zipOut.write(temp);
                }
            }
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Version 2 使用了Buffer
     * 100兆文件压缩，耗时：7秒（7248毫秒）
     */
    public static void zipFileBuffer() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOut);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(JPG_FILE));
            zipOut.putNextEntry(new ZipEntry(0 + SUFFIX_FILE));
            int temp;
            while ((temp = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(temp);
            }
            zipOut.closeEntry();
            bufferedInputStream.close();
            zipOut.close();
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 100兆文件压缩，耗时：3秒(3630毫秒)
     */
    public static void zipFileChannelBuffer() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            try (FileChannel fileChannel = new FileInputStream(JPG_FILE).getChannel()) {
                zipOut.putNextEntry(new ZipEntry(0 + SUFFIX_FILE));
                fileChannel.transferTo(0, FILE_SIZE, writableByteChannel);
            }
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Version 3 使用Channel
     * 100兆文件压缩，耗时：4秒（4559毫秒）
     */
    public static void zipFileChannel() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            try (FileChannel fileChannel = new FileInputStream(JPG_FILE).getChannel()) {
                zipOut.putNextEntry(new ZipEntry(0 + SUFFIX_FILE));
                fileChannel.transferTo(0, FILE_SIZE, writableByteChannel);
            }
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 100兆文件压缩，耗时：3秒(3452毫秒)
     */
    public static void zipFileMapBuffer() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            zipOut.putNextEntry(new ZipEntry(0 + SUFFIX_FILE));

            //内存中的映射文件
            MappedByteBuffer mappedByteBuffer = new RandomAccessFile(JPG_FILE_PATH, "r").getChannel()
                    .map(FileChannel.MapMode.READ_ONLY, 0, FILE_SIZE);

            writableByteChannel.write(mappedByteBuffer);
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Version 4 使用Map映射文件
     * 100兆文件压缩，耗时：4秒(4363毫秒)
     */
    public static void zipFileMap() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            zipOut.putNextEntry(new ZipEntry(0 + SUFFIX_FILE));

            //内存中的映射文件
            MappedByteBuffer mappedByteBuffer = new RandomAccessFile(JPG_FILE_PATH, "r").getChannel()
                    .map(FileChannel.MapMode.READ_ONLY, 0, FILE_SIZE);

            writableByteChannel.write(mappedByteBuffer);
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Version 5 使用Pip
     * 100兆文件压缩，耗时：7秒（7837毫秒）
     */
    public static void zipFilePip() {

        long beginTime = System.currentTimeMillis();
        try (WritableByteChannel out = Channels.newChannel(new FileOutputStream(ZIP_FILE))) {
            Pipe pipe = Pipe.open();
            //异步任务
            CompletableFuture.runAsync(() -> runTask(pipe));

            //获取读通道
            ReadableByteChannel readableByteChannel = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(((int) FILE_SIZE) * 10);
            while (readableByteChannel.read(buffer) >= 0) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printInfo(beginTime);

    }

    //异步任务
    private static void runTask(Pipe pipe) {

        try (ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
             WritableByteChannel out = Channels.newChannel(zos)) {

            zos.putNextEntry(new ZipEntry(0 + SUFFIX_FILE));
            FileChannel jpgChannel = new FileInputStream(new File(JPG_FILE_PATH)).getChannel();
            jpgChannel.transferTo(0, FILE_SIZE, out);
            jpgChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Version 6 使用Pip+Map
     * 100兆文件压缩，耗时：7秒(7006毫秒)
     */
    public static void zipFilePipMap() {
        long beginTime = System.currentTimeMillis();
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(ZIP_FILE));
             WritableByteChannel out = Channels.newChannel(fileOutputStream)) {
            Pipe pipe = Pipe.open();
            //异步任务往通道中塞入数据
            CompletableFuture.runAsync(() -> runTaskMap(pipe));
            //读取数据
            ReadableByteChannel workerChannel = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (workerChannel.read(buffer) >= 0) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
            printInfo(beginTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void test() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        try (FileOutputStream fos = new FileOutputStream(ZIP_FILE);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(JPG_FILE)
        ) {
            byte[] buffer = new byte[1024];

            zos.putNextEntry(new ZipEntry(0 + SUFFIX_FILE));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            printInfo(beginTime);
        } catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }
    }

    //异步任务
    private static void runTaskMap(Pipe pipe) {
        try (WritableByteChannel channel = pipe.sink();
             ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(channel));
             WritableByteChannel out = Channels.newChannel(zos)) {
            zos.putNextEntry(new ZipEntry(0 + SUFFIX_FILE));

            MappedByteBuffer mappedByteBuffer = new RandomAccessFile(
                    JPG_FILE_PATH, "r").getChannel()
                    .map(FileChannel.MapMode.READ_ONLY, 0, FILE_SIZE);
            out.write(mappedByteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInfo(long beginTime) {
        //耗时
        long timeConsum = (System.currentTimeMillis() - beginTime);

        System.out.println("fileSize:" + FILE_SIZE / 1024 / 1024 * 10 + "M");
        System.out.println("consum time:" + timeConsum + "毫秒");
        System.out.println("consum time:" + timeConsum / 1000 + "秒");
    }

}
