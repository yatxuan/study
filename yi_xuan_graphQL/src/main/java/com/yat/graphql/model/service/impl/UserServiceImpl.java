package com.yat.graphql.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yat.graphql.model.entity.UserEntity;
import com.yat.graphql.model.mapper.UserMapper;
import com.yat.graphql.model.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/9
 * @Time: 16:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
}
