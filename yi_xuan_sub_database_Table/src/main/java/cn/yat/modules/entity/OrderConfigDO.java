package cn.yat.modules.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("order_config")
public class OrderConfigDO {

    /**
     * 编号
     */
    @TableId
    private Integer id;
    /**
     * 支付超时时间
     *
     * 单位：分钟
     */
    private Integer payTimeout;

}
