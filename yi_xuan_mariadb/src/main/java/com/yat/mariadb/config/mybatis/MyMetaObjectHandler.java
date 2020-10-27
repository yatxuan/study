package com.yat.mariadb.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        // this.setFieldValByName("version", 1, metaObject);
        // this.setFieldValByName("deleted", 0, metaObject);
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());

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
