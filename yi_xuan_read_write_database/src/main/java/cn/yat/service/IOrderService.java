package cn.yat.service;

import cn.yat.entity.OrderDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/24
 * @Time: 15:25
 */
public interface IOrderService extends IService<OrderDO> {


    /**
     * 添加
     *
     * @param order 、
     */
    void add(OrderDO order);

    /**
     * 查询
     *
     * @param id 、
     * @return 、
     */
    OrderDO findById(Integer id);
}
