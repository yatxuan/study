package cn.yat.modules.mapper;

import cn.yat.modules.entity.OrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    OrderDO selectById(@Param("id") Integer id);

    List<OrderDO> selectListByUserId(@Param("userId") Integer userId);

    void insertOrder(OrderDO order);

}
