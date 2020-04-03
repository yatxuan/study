package com.yat.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * <p>Description: 描述 </p>
 * <p>@Import(FdfsClientConfig.class) ---> 导入FastDFS-Client组件</p>
 * <p>@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING) ---> 解决jmx重复注册bean的问题</p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/5
 * @Time: 14:03
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastDfsConfiguration {
}
