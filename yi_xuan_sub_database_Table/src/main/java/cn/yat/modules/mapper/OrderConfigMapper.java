package cn.yat.modules.mapper;

import cn.yat.modules.entity.OrderConfigDO;
import org.apache.ibatis.annotations.Param;

public interface OrderConfigMapper {

    OrderConfigDO selectById(@Param("id") Integer id);

}
