package com.yat.modules.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户角色关联
 *
 * @author Yat
 * @email
 * @date 2020-03-25 16:50:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user_roles")
public class UserRolesEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;

}
