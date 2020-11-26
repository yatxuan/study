package com.yat.prototype;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/26 - 11:30
 */
@Getter
@Setter
public abstract class Prototype implements Cloneable {

    /**
     * 文件ID
     */
    private Integer fileId;
    /**
     * 文件名
     */
    private String filename;

    /**
     * 复杂对象-文件打分
     */
    private Map<String, Double> scores;
    /**
     * 复杂对象-数据
     */
    private ArrayList<Integer> list;
    /**
     * 复杂对象-文件类
     */
    private Deep deep;

    @Override
    public Prototype clone() {
        Prototype prototype = null;
        try {
            // 有下面这句话就能实现浅拷贝
            prototype = (Prototype) super.clone();
            //  要实现深拷贝，就要对每一个复杂对象进行手动克隆，以当前对象里面的复杂对象为例
            prototype.scores = (Map<String, Double>) ((HashMap) this.scores).clone();
            prototype.list = (ArrayList) this.list.clone();
            prototype.deep = this.deep.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return prototype;
    }

    public Prototype() {
        System.out.println("构造方法--Prototype");
    }

    public Prototype(Integer fileId, String filename, Map<String, Double> scores, ArrayList<Integer> list, Deep deep) {
        System.out.println("构造方法--Prototype");
        this.fileId = fileId;
        this.filename = filename;
        this.scores = scores;
        this.list = list;
        this.deep = deep;
    }

    @Override
    public String toString() {
        return "Prototype{" +
                "fileId=" + fileId +
                ", filename='" + filename + '\'' +
                ", scores=" + scores +
                ", deep=" + deep +
                '}';
    }
}
