package com.yat.split.file.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/2 - 17:14
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "test")
public class Test {

    private String name;
    private int age;

}
