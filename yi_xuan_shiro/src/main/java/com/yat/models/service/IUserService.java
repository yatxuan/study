package com.yat.models.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yat.models.entity.RoleEntity;
import com.yat.models.entity.UserEntity;

import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/6/20 - 14:40
 */
public interface IUserService extends IService<UserEntity> {

    /**
     * 查询用户角色信息
     * @param id 用户id
     * @return 、
     */
    List<RoleEntity> findUserRoles(Long id);
}
