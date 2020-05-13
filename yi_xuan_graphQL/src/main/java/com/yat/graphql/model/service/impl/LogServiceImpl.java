package com.yat.graphql.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yat.graphql.model.entity.LogEntity;
import com.yat.graphql.model.mapper.LogMapper;
import com.yat.graphql.model.service.ILogService;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/9
 * @Time: 16:05
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements ILogService {

}
