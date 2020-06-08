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
@RabbitListener(queues = "topic.b")
public class TopicConsumerB {

    /**
     * 消息消费
     *
     * <p> @RabbitHandler 代表此方法为接受到消息后的处理方法</p>
     */
    @RabbitHandler
    public void received(String msg) {
        log.info("主题模式:[topic.b] 收到消息: {}", msg);
    }
}
