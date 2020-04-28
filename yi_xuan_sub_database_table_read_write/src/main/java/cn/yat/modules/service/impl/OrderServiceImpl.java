package cn.yat.modules.service.impl;

import cn.yat.modules.entity.OrderDO;
import cn.yat.modules.mapper.OrderMapper;
import cn.yat.modules.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/24
 * @Time: 16:44
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {
}
