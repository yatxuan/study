package com.yat.config.mybatis;


import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>Description: MybatisPlus配置 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/20
 * @Time: 11:17
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.yat.modules.mapper")
public class MybatisPlusConfig {

    /**
     * 乐观锁插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }


    /** 3.0.5及以下版本需要配置以下参数才能生效,当前版本无需在此配置
     * 逻辑删除
     */
    // @Bean
    // public ISqlInjector sqlInjector() {
    //     return new LogicSqlInjector();
    // }


    /**
     * 3.0.5及以下版本需要配置以下参数才能生效,当前版本无需在此配置
     * SQL 执行性能分析插件
     * 开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长
     *
     * <p> @Profile({"dev", "test"}) 设置 dev-开发环境 test-测试环境 开启</p>
     * <p> prod-生产环境，pred-灰度环境，test-测试环境，dev-开发环境</p>
     */
    // @Bean
    // @Profile({"dev", "test"})
    // public PerformanceInterceptor  performanceInterceptor() {
    //     PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
    //     //ms，超过此处设置的ms则sql不执行
    //     performanceInterceptor.setMaxTime(100);
    //     performanceInterceptor.setFormat(true);
    //     return performanceInterceptor;
    // }

}
