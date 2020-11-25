package com.yat.factory.method.message;

/**
 * <p>Description: 工厂方法模式_sms产品 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/25 16:27
 */
public class MyMessageSms extends MyAbstractMessage {

    @Override
    public void sendMessage() throws Exception {
        // TODO Auto-generated method stub
        if (null == getMessageParam()
                || null == getMessageParam().get("PHONENUM")
                || "".equals(getMessageParam().get("PHONENUM"))) {
            // 为了简单起见异常也不自定义了
            throw new Exception("发送短信,需要传入PHONENUM参数");
        }// 另外短信信息，以及其他各种协议参数等等都要处理

        System.out.println("我是短信，发送通知给" + getMessageParam().get("PHONENUM"));
    }

}
