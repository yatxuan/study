package com.yat.mariadb.modules.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yat.mariadb.modules.entity.UserEntity;

import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 15:25
 */
public interface IUserService extends IService<UserEntity> {

    /**
     * XML 自定义分页
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param age  查询条件
     * @return 分页对象
     */
    IPage<UserEntity> selectUserPage(IPage<UserEntity> page, Integer age);

    /**
     * XML 自定义分页
     *
     * @param params
     * @return 分页对象
     */
    IPage<UserEntity> selectUserPage(Map<String, Object> params);
}
