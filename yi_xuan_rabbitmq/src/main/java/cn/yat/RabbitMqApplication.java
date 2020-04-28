package cn.yat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p>Description:  </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/26
 * @Time: 15:59
 */
@SpringBootApplication
public class RabbitMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class);
    }
}
