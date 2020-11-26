package com.yat.proxy;

import com.yat.proxy.service.BuyHouse;
import com.yat.proxy.service.impl.BuyHouseImpl;
import com.yat.proxy.impl.BuyHouseProxy;

/**
 * <p>Description: 静态代理：测试类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 15:51
 */
public class StaticProxyTest {

    public static void main(String[] args) {
        System.out.println("----------------------代理前----------------------");
        BuyHouse buyHouse = new BuyHouseImpl();
        buyHouse.buyHouse();
        System.out.println("----------------------代理后----------------------");
        BuyHouseProxy buyHouseProxy = new BuyHouseProxy(buyHouse);
        buyHouseProxy.buyHouse();
    }
}
