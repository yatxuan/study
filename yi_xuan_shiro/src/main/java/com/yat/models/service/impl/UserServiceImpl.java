package com.yat.models.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yat.models.entity.RoleEntity;
import com.yat.models.entity.UserEntity;
import com.yat.models.entity.UserRolesEntity;
import com.yat.models.mapper.UserMapper;
import com.yat.models.service.IRoleService;
import com.yat.models.service.IUserRoleService;
import com.yat.models.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/6/20 - 14:40
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

    private final IUserRoleService userRoleService;
    private final IRoleService roleService;

    @Override
    public List<RoleEntity> findUserRoles(Long id) {

        // 查询该用户所有的角色数据
        List<UserRolesEntity> list = userRoleService.list(
                new LambdaQueryWrapper<UserRolesEntity>()
                        .eq(UserRolesEntity::getUserId, id)
        );
        if (list.isEmpty()) {
           return new ArrayList<>(1);
        }

        // 获取角色id
        Set<Long> roleIds = list.stream().map(UserRolesEntity::getRoleId).collect(Collectors.toSet());

        return roleService.list(
                new LambdaQueryWrapper<RoleEntity>()
                        .in(RoleEntity::getId, roleIds)
        );
    }
}
