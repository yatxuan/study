package com.yat.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import io.netty.util.internal.SystemPropertyUtil;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author ynz
 * @version 创建时间：2018/6/26
 * @email ynz@myframe.cn
 */
@ConfigurationProperties(prefix = "netty.file")
@Data
@Validated
public class NettyFileProperties {

    @NotNull(message = "端口不能为空")
    @Range(min = 1000, max = 60000)
    private Integer port;

    @NotNull(message = "文件路径不能为空")
    private String path;

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
