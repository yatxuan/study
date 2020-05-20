package com.yat.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yat.modules.entity.UserEntity;
import com.yat.modules.mapper.UserMapper;
import com.yat.modules.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/20
 * @Time: 10:43
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
}
