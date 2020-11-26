package com.yat.builder;

/**
 * <p>Description: Product（产品角色）:一个具体的产品对象。 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/26 11:01
 */
public class Meal {
    /**
     * 食物
     */
    private String food;
    /**
     * 饮料
     */
    private String drink;

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }
}
