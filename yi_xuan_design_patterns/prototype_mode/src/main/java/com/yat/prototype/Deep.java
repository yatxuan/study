package com.yat.prototype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/26 - 11:36
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Deep implements Cloneable {
    private String name;

    public Deep(String name) {
        System.out.println("构造方法--deep");
        this.name = name;
    }

    @Override
    protected Deep clone() {
        Deep deep = null;
        try {
            deep = (Deep) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return deep;
    }

}
