package com.yat.builder;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 11:04
 */
public class Test {

    public static void main(String[] args) {
        // 套餐A
        MealA a = new MealA();
        // 准备套餐A的服务员
        KFCWaiter waiter = new KFCWaiter(a);
        // 获得套餐
        Meal mealA = waiter.construct();
        System.out.print("套餐A的组成部分:");
        System.out.println("食物：" + mealA.getFood() + "；   " + "饮品：" + mealA.getDrink());
    }
}
