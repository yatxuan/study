package cn.yat.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/28 13:50
 */
@Slf4j
@Component
@SuppressWarnings("all")
@RabbitListener(queues = "fanout.a")
public class FanoutAConsumer {

    /**
     * 消息消费
     *
     * @RabbitHandler 代表此方法为接受到消息后的处理方法
     */
    @RabbitHandler
    public void received(String msg) {
        log.info("广播模式：[fanout.a] 收到消息: {}", msg);
    }
}