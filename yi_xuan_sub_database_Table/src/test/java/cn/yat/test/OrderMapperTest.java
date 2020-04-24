package cn.yat.test;

import cn.yat.SubDataTableApplication;
import cn.yat.modules.entity.OrderDO;
import cn.yat.modules.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/24
 * @Time: 14:44
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubDataTableApplication.class)
@SuppressWarnings("all")
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testSelectById() {
        OrderDO order = orderMapper.selectById(1);
        System.out.println(order);
    }

    @Test
    public void testSelectListByUserId() {
        List<OrderDO> orders = orderMapper.selectListByUserId(1);
        System.out.println(orders.size());
    }

    @Test
    public void testInsert() {
        OrderDO order = new OrderDO();
        for (int i = 0; i < 1000; i++) {
            order.setUserId(i);
            orderMapper.insertOrder(order);
        }
    }
}
