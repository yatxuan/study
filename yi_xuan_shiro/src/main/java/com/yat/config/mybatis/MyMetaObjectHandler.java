package com.yat.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yat.common.constant.UserConstant;
import com.yat.modules.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
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
        this.setFieldValByName("createTime", date, metaObject);
        this.setFieldValByName("updateTime", date, metaObject);
        this.setFieldValByName("version", 1, metaObject);
        this.setFieldValByName("enabled", UserConstant.STATUS_NORMAL, metaObject);
        this.setFieldValByName("deleted", UserConstant.STATUS_NORMAL, metaObject);

        UserEntity userEntity = (UserEntity) SecurityUtils.getSubject().getPrincipal();
        this.setFieldValByName("createBy", userEntity.getUsername(), metaObject);
    }

    /**
     * 在修改的时候，为配置的字段插入指定的数据
     *
     * @param metaObject 、
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        UserEntity userEntity = (UserEntity) SecurityUtils.getSubject().getPrincipal();

        this.setFieldValByName("updateBy", userEntity.getUsername(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
