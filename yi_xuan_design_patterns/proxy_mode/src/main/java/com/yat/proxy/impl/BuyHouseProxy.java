package com.yat.proxy.impl;


import com.yat.proxy.service.BuyHouse;

/**
 * <p>Description: 创建静态代理类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 15:50
 */
public class BuyHouseProxy implements BuyHouse {

    private BuyHouse buyHouse;

    public BuyHouseProxy(final BuyHouse buyHouse) {
        this.buyHouse = buyHouse;
    }

    @Override
    public void buyHouse() {
        System.out.println("买房前准备");
        buyHouse.buyHouse();
        System.out.println("买房后装修");

    }
}
