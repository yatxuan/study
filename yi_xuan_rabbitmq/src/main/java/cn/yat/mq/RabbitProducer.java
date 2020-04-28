package cn.yat.mq;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RabbitProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendDemoQueue() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-mm-DD hh:MM:ss").format(date);
        log.info("[demoQueue] send msg: {}", dateString);
        // 第一个参数为刚刚定义的队列名称
        this.rabbitTemplate.convertAndSend("demoQueue", dateString);
    }

    public void sendFanout() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-mm-DD hh:MM:ss").format(date);
        log.info("[fanout] send msg: {}", dateString);
        // 注意 第一个参数是我们交换机的名称 ，第二个参数是routerKey 我们不用管空着就可以，第三个是你要发送的消息
        this.rabbitTemplate.convertAndSend("fanoutExchange", "", dateString);
    }

    public void sendTopicTopicAB() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-mm-DD hh:MM:ss").format(date);
        dateString = "[topic.msg] send msg:" + dateString;
        log.info(dateString);
        // 注意 第一个参数是我们交换机的名称 ，第二个参数是routerKey topic.msg，第三个是你要发送的消息
        // 这条信息将会被 topic.a  topic.b接收
        this.rabbitTemplate.convertAndSend("topicExchange", "topic.msg", dateString);
    }

    public void sendTopicTopicB() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-mm-DD hh:MM:ss").format(date);
        dateString = "[topic.good.msg] send msg:" + dateString;
        log.info(dateString);
        // 注意 第一个参数是我们交换机的名称 ，第二个参数是routerKey ，第三个是你要发送的消息
        // 这条信息将会被topic.b接收
        this.rabbitTemplate.convertAndSend("topicExchange", "topic.good.msg", dateString);
    }

    public void sendTopicTopicBC() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-mm-DD hh:MM:ss").format(date);
        dateString = "[topic.m.z] send msg:" + dateString;
        log.info(dateString);
        // 注意 第一个参数是我们交换机的名称 ，第二个参数是routerKey ，第三个是你要发送的消息
        // 这条信息将会被topic.b、topic.c接收
        this.rabbitTemplate.convertAndSend("topicExchange", "topic.m.z", dateString);
    }
}
