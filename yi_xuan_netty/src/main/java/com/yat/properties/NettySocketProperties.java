package com.yat.properties;

import io.netty.util.internal.SystemPropertyUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/9/17 11:29
 */
@ConfigurationProperties(prefix = "netty.socket")
@Data
@Validated
public class NettySocketProperties {

    @NotNull(message = "端口不能为空")
    @Range(min = 1000, max = 60000)
    private Integer port;

    @Pattern(regexp = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))", message = "ip地址格式不正确")
    private String bindIp;

    /**
     * 必须大于1
     */
    @DecimalMin("1")
    private Integer bossThreads = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));
    /**
     * 必须大于1
     */
    @DecimalMin("1")
    private Integer workThreads = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));

}
