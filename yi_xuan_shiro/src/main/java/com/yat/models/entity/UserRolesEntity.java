package com.yat.models.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>Description: 用户角色关联 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 21:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user_roles")
public class UserRolesEntity implements Serializable {

    private static final long serialVersionUID = -7979431676529939628L;
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
