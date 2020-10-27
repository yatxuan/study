package com.yat.mariadb.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yat.mariadb.modules.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/10/26 - 15:21
 */
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * <p>
     * 查询 : 根据state状态查询用户列表，分页显示
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param age  查询条件
     * @return 分页对象
     */
    IPage<UserEntity> selectPageVo(@Param("page") IPage<UserEntity> page, @Param("age") Integer age);
}
