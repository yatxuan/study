package com.yat.models.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>Description: 角色权限关联 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 21:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_role_permissions")
public class RolePermissionsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 权限ID
     */
    private Long permissionId;
}
