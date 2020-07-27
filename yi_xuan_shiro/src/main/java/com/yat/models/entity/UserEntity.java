package com.yat.models.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yat.models.entity.base.BaseEntity;
import lombok.*;

/**
 * <p>Description: 系统用户表 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 21:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = 2180152431279445394L;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户状态：1启用、-1禁用
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer enabled;
    /**
     * 乐观锁：版本标识(占时不用)
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    /**
     * 设置每个账号最多同时几个客户端登录：允许最大的登陆人数
     */
    private Integer logNumber;
}
