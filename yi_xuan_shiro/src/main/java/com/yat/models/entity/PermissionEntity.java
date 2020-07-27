package com.yat.models.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yat.models.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 权限表
 *
 * @author 熊能
 * @version 1.0
 * @since 2018/01/02
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tb_permission")
public class PermissionEntity extends BaseEntity {

    private static final long serialVersionUID = -9180928048447375376L;
    /**
     * 权限名称
     */
    private String permission;
    /**
     * 权限说明
     */
    private String description;

}
