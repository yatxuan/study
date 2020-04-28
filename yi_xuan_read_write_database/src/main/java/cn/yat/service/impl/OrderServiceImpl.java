package cn.yat.service.impl;

import cn.yat.entity.OrderDO;
import cn.yat.mapper.OrderMapper;
import cn.yat.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
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
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void add(OrderDO order) {
        // <1.1> 这里先假模假样的读取一下。读取从库
        OrderDO exists = this.baseMapper.selectById(1);
        log.info("读取从库: '{}'", exists);

        // <1.2> 插入订单
        this.baseMapper.insert(order);
        log.info("插入订单: '{}'", order);

        // <1.3> 这里先假模假样的读取一下。读取主库 在执行新增语句后，会默认查询主库
        // （    占时还不明白原因：可能原因是：因为增删改 默认都是使用的主库的数据库，当执行这些命令后，
        //      当前数据源会从 ‘从库’ 转变到 ‘主库’，所以在执行查询语句时，会使用主库的数据源，而不会切换到从库的数据源）
        OrderDO exists1 = this.baseMapper.selectById(1);
        log.info("读取主库: '{}'", exists1);

        // <1.4> 强制查询主库
        try (HintManager hintManager = HintManager.getInstance()) {
            // 设置强制访问主库
            hintManager.setMasterRouteOnly();
            // 执行查询
            OrderDO order2 = this.baseMapper.selectById(1);
            log.info("读取主库: '{}'", order2);
        }
    }

    @Override
    public OrderDO findById(Integer id) {
        return this.baseMapper.selectById(id);
    }
}
