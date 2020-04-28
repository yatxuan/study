package cn.yat.controller;

import cn.yat.mq.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/4/28 13:50
 */
@RestController
@SuppressWarnings("all")
public class RabbitMqController {

    @Autowired
    private RabbitProducer rabbitProducer;

    @GetMapping("/sendDemoQueue")
    public Object sendDemoQueue() {
        rabbitProducer.sendDemoQueue();
        return "success";
    }

    @GetMapping("/sendFanout")
    public Object sendFanout() {
        rabbitProducer.sendFanout();
        return "success";
    }

    @GetMapping("/sendTopicTopicAB")
    public Object sendTopicTopicAB() {
        rabbitProducer.sendTopicTopicAB();
        return "success";
    }

    @GetMapping("/sendTopicTopicB")
    public Object sendTopicTopicB() {
        rabbitProducer.sendTopicTopicB();
        return "success";
    }

    @GetMapping("/sendTopicTopicBC")
    public Object sendTopicTopicBC() {
        rabbitProducer.sendTopicTopicBC();
        return "success";
    }
}
