package com.yat.builder;

/**
 * <p>Description:  ConcreteBuilder（具体建造者）:实现抽象接口，构建和装配各个部件。 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 11:02
 */
public class MealB extends MealBuilder {

    @Override
    public void buildDrink() {
        meal.setDrink("柠檬果汁");
    }

    @Override
    public void buildFood() {
        meal.setFood("鸡翅");
    }

}
