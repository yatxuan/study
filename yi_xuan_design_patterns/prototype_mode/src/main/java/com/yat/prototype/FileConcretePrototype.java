package com.yat.prototype;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/26 - 11:43
 */
@NoArgsConstructor
public class FileConcretePrototype extends Prototype {

    public FileConcretePrototype(Integer fileId, String filename, Map<String, Double> scores, ArrayList<Integer> list, Deep deep) {
        super(fileId, filename, scores, list, deep);
        System.out.println("构造方法-----------FileConcretePrototype");
    }

    public void show() {
        System.out.println("-----------文件信息-----------");
        System.out.println("文件ID：" + this.getFileId());
        System.out.println("文件名：" + this.getFilename());
        System.out.println("文件打分：" + this.getScores());
        System.out.println("文件数据：" + this.getList());
        System.out.println("文件类：" + this.getDeep());
    }
}
