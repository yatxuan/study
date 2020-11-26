package com.yat.builder;

/**
 * <p>Description: Builder（抽象建造者）:创建一个Product对象的各个部件指定的抽象接口。 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 11:01
 */
public abstract class MealBuilder {
    Meal meal = new Meal();

    /**
     * 选择食物
     */
    public abstract void buildFood();

    /**
     * 选择饮料
     */
    public abstract void buildDrink();

    public Meal getMeal() {
        return meal;
    }
}
