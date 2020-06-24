package com.yat.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yat.common.constant.UserConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>Description: 实现元对象处理器接口 </p>
 *
 * @author Yat-Xuan
 * @date 2020/5/20 11:12
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 在新增的时候，为配置的字段插入指定的数据
     *
     * @param metaObject 、
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Date date = new Date();
        // 新增的时候自动插入 创建时间
        this.setFieldValByName("createTime", date, metaObject);
        // 新增的时候自动插入 修改时间
        this.setFieldValByName("updateTime", date, metaObject);
        // 新增的时候自动插入 版本
        this.setFieldValByName("version", 1, metaObject);
        // 新增的时候自动插入 状态 正常:1 禁用:-1
        this.setFieldValByName("deleted", UserConstant.STATUS_NORMAL, metaObject);
    }

    /**
     * 在修改的时候，为配置的字段插入指定的数据
     *
     * @param metaObject 、
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");

        // 新增的时候自动插入 修改时间
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
