package com.yat.mariadb.modules.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yat.mariadb.config.mybatis.Query;
import com.yat.mariadb.modules.entity.UserEntity;
import com.yat.mariadb.modules.mapper.UserMapper;
import com.yat.mariadb.modules.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 15:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

    @Override
    public IPage<UserEntity> selectUserPage(IPage<UserEntity> page, Integer age) {
        // 当 total 为小于 0 或者设置 setSearchCount(false) 分页插件不会进行 count 查询
        // 要点!! 分页返回的对象与传入的对象是同一个
        return baseMapper.selectPageVo(page, age);
    }

    @Override
    public IPage<UserEntity> selectUserPage(Map<String, Object> params) {

        IPage<UserEntity> page = new Query<UserEntity>().getPage(
                params,
                UserEntity::getAge,
                true
        );

        return selectUserPage(page, 1);
    }


}
