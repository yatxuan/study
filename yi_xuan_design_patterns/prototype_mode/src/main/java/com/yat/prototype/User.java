package com.yat.prototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 定义用户去模拟过程 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/26 - 11:49
 */
public class User {

    public static void main(String[] args) {
        // 文件ID
        Integer fileId = 123;
        // 文件名
        String filename = "重要文件";
        Map<String, Double> scores = new HashMap<>(1);
        scores.put("张三", 99.99);
        Deep deep = new Deep("文件类");
        ArrayList<Integer> list = new ArrayList<>(3);
        list.add(123);
        list.add(465);
        list.add(789);

        FileConcretePrototype fileA = new FileConcretePrototype(fileId, filename, scores, list, deep);

        FileConcretePrototype fileB = (FileConcretePrototype) fileA.clone();
        FileConcretePrototype fileC = (FileConcretePrototype) fileA.clone();

        fileA.show();
        fileB.getDeep().setName("次要文件");
        fileB.setFilename("filename");
        fileB.show();
        fileC.show();
        fileA.show();

        List<FileConcretePrototype> fileList = new ArrayList<>(10);

        for (int i = 0, num = 10; i < num; i++) {
            FileConcretePrototype fileD = (FileConcretePrototype) fileA.clone();
            fileD.setFilename("循环次数：" + i);
            fileD.getDeep().setName("次要文件:" + i);
            fileList.add(fileD);
        }

        fileA.show();
        System.out.println(fileList);
    }
}
