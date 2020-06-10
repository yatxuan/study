package cn.yat.test;

import cn.yat.SubDataTableApplication;
import cn.yat.modules.common.id.IdWorker;
import cn.yat.modules.entity.OrderDO;
import cn.yat.modules.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@Slf4j
@RequiredArgsConstructor
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubDataTableApplication.class)
public class OrderMapperTest {

    private final IOrderService orderService;
    private final IdWorker idWorker;

    @Test
    public void testSelectById() {
        OrderDO order = orderService.getById(1255036824873689088L);
        log.info("order: '{}'", order);
    }

    @Test
    public void testSelectListByUserId() {
        List<OrderDO> orders = orderService.list();
        log.info("size: '{}'", orders.size());
    }

    @Test
    public void testInsert() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        OrderDO order = new OrderDO();
        long id;
        for (int i = 0; i < 1000; i++) {
            id = idWorker.nextId();
            order.setId(id);
            order.setUserId(i);
            orderService.save(order);
            log.info("i: '{}',id: '{}',order: '{}'", i, id, order);
        }
        //耗时
        long timeConsum = (System.currentTimeMillis() - beginTime);

        System.out.println("consum time:" + timeConsum + "毫秒");
        System.out.println("consum time:" + timeConsum / 1000 + "秒");
    }

    @Test
    public void testDel(){
        orderService.removeById(1255036824873689088L);
    }
}
