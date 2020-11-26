package com.yat.builder;

/**
 * <p>Description:  ConcreteBuilder（具体建造者）:实现抽象接口，构建和装配各个部件。 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 11:02
 */
public class MealA extends MealBuilder{

    @Override
    public void buildDrink() {
        meal.setDrink("可乐");
    }

    @Override
    public void buildFood() {
        meal.setFood("薯条");
    }

}
