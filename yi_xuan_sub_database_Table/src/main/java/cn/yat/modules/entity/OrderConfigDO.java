package cn.yat.modules.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单配置 DO
 *  @author yat
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfigDO {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 支付超时时间
     *
     * 单位：分钟
     */
    private Integer payTimeout;

}
