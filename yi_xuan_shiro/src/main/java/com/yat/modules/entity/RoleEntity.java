package com.yat.modules.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yat.modules.entity.base.BaseEntity;
import lombok.*;

/**
 * 角色表
 *
 * @author Yat
 * @email
 * @date 2020-03-25 16:50:42
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_role")
public class RoleEntity extends BaseEntity {
    /**
     * 角色名称
     */
    private String role;
    /**
     * 角色备注
     */
    private String remark;

}
