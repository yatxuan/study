package com.yat.factory.method;

import java.util.HashMap;
import java.util.Map;

import com.yat.factory.method.message.IMyMessage;
import com.yat.factory.method.message.MyMessageEmail;
import com.yat.factory.method.message.MyMessageOaTodo;
import com.yat.factory.method.message.MyMessageSms;

/**
 * <p>Description: 工厂方法模式_工厂实现 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/25 16:23
 */
public class MyMessageFactory implements IMyMessageFactory {

    @Override
    public IMyMessage createMessage(String messageType) {
        // 这里的方式是：消费者知道自己想要什么产品；若生产何种产品完全由工厂决定，则这里不应该传入控制生产的参数。
        IMyMessage myMessage;
        Map<String, Object> messageParam = new HashMap<>(16);
        // 根据某些条件去选择究竟创建哪一个具体的实现对象，条件可以传入的，也可以从其它途径获取。
        if ("SMS".equals(messageType)) {
            // sms
            myMessage = new MyMessageSms();
            messageParam.put("PHONENUM", "123456789");
        } else if ("OA".equals(messageType)) {
            // OA待办
            myMessage = new MyMessageOaTodo();
            messageParam.put("OAUSERNAME", "testUser");
        } else if ("EMAIL".equals(messageType)) {
            // email
            myMessage = new MyMessageEmail();
            messageParam.put("EMAIL", "test@test.com");
        } else {
            // 默认生产email这个产品
            myMessage = new MyMessageEmail();
            messageParam.put("EMAIL", "test@test.com");
        }
        myMessage.setMessageParam(messageParam);
        return myMessage;
    }
}
