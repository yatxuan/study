package com.yat.models.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yat.models.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Description: 角色表 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 21:49
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
