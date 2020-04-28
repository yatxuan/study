package cn.yat.modules.service.impl;

import cn.yat.modules.entity.UserEntity;
import cn.yat.modules.mapper.UserMapper;
import cn.yat.modules.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/28
 * @Time: 14:28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
}
