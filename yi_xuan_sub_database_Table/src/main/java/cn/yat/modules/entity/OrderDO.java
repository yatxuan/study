package cn.yat.modules.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单 DO
 * @author yat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDO {

    /**
     * 订单编号
     */
    private Long id;
    /**
     * 用户编号
     */
    private Integer userId;

}
