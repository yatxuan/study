package com.yat.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
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
        this.strictInsertFill(metaObject, "createTime", Date.class, date);
        this.strictInsertFill(metaObject, "updateTime", Date.class, date);
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
    }

    /**
     * 在修改的时候，为配置的字段插入指定的数据
     *
     * @param metaObject 、
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");

        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}
