package cn.yat.modules.service.impl;

import cn.yat.modules.entity.OrderConfigDO;
import cn.yat.modules.mapper.OrderConfigMapper;
import cn.yat.modules.service.IOrderConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/24
 * @Time: 16:45
 */
@Service
public class OrderConfigServiceImpl extends ServiceImpl<OrderConfigMapper, OrderConfigDO> implements IOrderConfigService {
}
