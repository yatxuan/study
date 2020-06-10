package cn.yat.test;

import cn.yat.modules.SubDataTableReadWriteApplication;
import cn.yat.modules.entity.OrderConfigDO;
import cn.yat.modules.service.IOrderConfigService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/24
 * @Time: 14:34
 */
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
@SpringBootTest(classes = SubDataTableReadWriteApplication.class)
public class OrderConfigMapperTest {

    private final IOrderConfigService orderConfigService;

    @Test
    public void testSelectById() {
        OrderConfigDO orderConfig = orderConfigService.getById(1);
        System.out.println(orderConfig);
    }

}
