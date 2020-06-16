package com.yat.modules.entity.base;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/16
 * @Time: 18:08
 */
@Getter
@Setter
public class BaseEntity {

    /**
     * <p>@TableId(type = IdType.ID_WORKER):自定义注解自增方式为，全局唯一ID (idWorker)</p>
     * <p>如果在yaml配置文件中配置了，这里就不用配置了</p>
     * <p>如果实体类配置了自增策略，默认以实体类里配置的为主</p>
     */
    @TableId
    private Long id;
}
