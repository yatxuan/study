package cn.yat.service.impl;

import cn.yat.entity.OrderDO;
import cn.yat.mapper.OrderMapper;
import cn.yat.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/24
 * @Time: 15:26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void add(OrderDO order) {
        // <1.1> 这里先假模假样的读取一下。读取从库
        OrderDO exists = this.baseMapper.selectById(1);
        System.out.println("读取从库:" + exists);

        // <1.2> 插入订单
        this.baseMapper.insert(order);
        System.out.println("插入订单:" + order);

        // <1.3> 这里先假模假样的读取一下。读取主库
        exists = this.baseMapper.selectById(1);
        System.out.println("读取主库:" + exists);
    }

    @Override
    public OrderDO findById(Integer id) {
        return this.baseMapper.selectById(id);
    }
}
