package com.yat.modules.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.yat.modules.entity.base.BaseEntity;
import lombok.*;

import java.util.Date;

/**
 * 系统用户
 *
 * @author Yat
 * @email
 * @date 2020-03-25 16:50:42
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class UserEntity extends BaseEntity {
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
     * 乐观锁：版本标识
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
