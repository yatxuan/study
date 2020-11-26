package com.yat.proxy;

import com.yat.proxy.service.BuyHouse;
import com.yat.proxy.service.impl.BuyHouseImpl;
import com.yat.proxy.impl.DynamicProxyHandler;

import java.lang.reflect.Proxy;

/**
 * <p>Description: 动态代理：测试类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 16:00
 */
public class DynamicProxyTest {

    public static void main(String[] args) {
        BuyHouse buyHouse = new BuyHouseImpl();
        BuyHouse proxyBuyHouse = (BuyHouse) Proxy.newProxyInstance(
                BuyHouse.class.getClassLoader(),
                new Class[]{BuyHouse.class},
                new DynamicProxyHandler(buyHouse)
        );
        proxyBuyHouse.buyHouse();
    }
}
