package cn.yat.test;

import cn.yat.ReadWriterApplication;
import cn.yat.entity.OrderDO;
import cn.yat.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@RequiredArgsConstructor
@SpringBootTest(classes = ReadWriterApplication.class)
public class OrderServiceTest {

    private final IOrderService orderService;

    @Test
    public void testAdd() {
        OrderDO order = new OrderDO();
        order.setUserId(20);
        orderService.add(order);
    }

    @Test
    public void testFindById() {
        OrderDO order = orderService.findById(1);
        System.out.println(order);
    }

}
