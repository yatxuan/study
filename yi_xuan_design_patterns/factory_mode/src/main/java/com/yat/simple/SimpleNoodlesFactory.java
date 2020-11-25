package com.yat.simple;

import com.yat.simple.service.INoodleService;
import com.yat.simple.service.impl.GankouNoodleServiceImpl;
import com.yat.simple.service.impl.LzNoodleServiceImpl;
import com.yat.simple.service.impl.PaoNoodleServiceImpl;

/**
 * <p>Description: 简单工厂模式 </p>
 * <hr/>
 * <p>
 * 特点:<br>
 * 1 它是一个具体的类，非接口 抽象类。有一个重要的create()方法，利用if或者 switch创建产品并返回。<br>
 * 2 create()方法通常是静态的，所以也称之为静态工厂。
 * </p>
 * <hr/>
 * <p>
 * 缺点<br>
 * 1 扩展性差（我想增加一种面条，除了新增一个面条产品类，还需要修改工厂类方法）<br>
 * 2 不同的产品需要不同额外参数的时候 不支持。
 * </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/25 15:54
 */
public class SimpleNoodlesFactory {

    /**
     * 兰州拉面
     */
    private static final int TYPE_LZ = 1;
    /**
     * 泡面
     */
    private static final int TYPE_PM = 2;
    /**
     * 干扣面
     */
    private static final int TYPE_GK = 3;

    private static INoodleService createNoodles(int type) {
        switch (type) {
            case TYPE_LZ:
                return new LzNoodleServiceImpl();
            case TYPE_PM:
                return new PaoNoodleServiceImpl();
            case TYPE_GK:
            default:
                return new GankouNoodleServiceImpl();
        }
    }

    public static void main(String[] args) {
        INoodleService noodles = SimpleNoodlesFactory.createNoodles(SimpleNoodlesFactory.TYPE_LZ);
        noodles.desc();
    }
}
