package com.yat.factory.method.message;

/**
 * <p>Description: 工厂方法模式_OA待办产品 </p>
 *
 * @author Yat-Xuan
 * @date 2020/11/25 16:26
 */
public class MyMessageOaTodo extends MyAbstractMessage {

    @Override
    public void sendMessage() throws Exception {
        // TODO Auto-generated method stub
        if (null == getMessageParam()
                || null == getMessageParam().get("OAUSERNAME")
                || "".equals(getMessageParam().get("OAUSERNAME"))) {
            // 为了简单起见异常也不自定义了
            throw new Exception("发送OA待办,需要传入OAUSERNAME参数");
        }// 这里的参数需求就比较多了不一一处理了

        System.out.println("我是OA待办，发送通知给" + getMessageParam().get("OAUSERNAME"));
    }

}
