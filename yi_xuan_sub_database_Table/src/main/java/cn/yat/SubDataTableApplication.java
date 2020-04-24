package cn.yat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/24
 * @Time: 13:46
 */
@SpringBootApplication
@MapperScan("cn.yat.modules.mapper")
public class SubDataTableApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubDataTableApplication.class);
    }
}
