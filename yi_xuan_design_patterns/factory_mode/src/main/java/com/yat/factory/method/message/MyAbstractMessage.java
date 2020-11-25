package com.yat.factory.method.message;

import java.util.Map;

/**
 * <p>Description: 工厂方法模式_虚拟产品类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/25 16:24
 */
public abstract class MyAbstractMessage implements IMyMessage {

    /**
     * 这里可以理解为生产产品所需要的原材料库。最好是个自定义的对象，这里为了不引起误解使用Map。
     */
    private Map<String, Object> messageParam;

    @Override
    public Map<String, Object> getMessageParam() {
        return messageParam;
    }

    @Override
    public void setMessageParam(Map<String, Object> messageParam) {
        this.messageParam = messageParam;
    }
}
