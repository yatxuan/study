package com.yat.mariadb.modules.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yat.mariadb.modules.entity.base.BaseEntity;
import lombok.*;

import java.util.Date;

/**
 * <p>Description: 用户表 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 15:24
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class UserEntity  extends BaseEntity {

    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     * <p> @TableField(fill = FieldFill.INSERT):在新增时自动插入创建时间</p>
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     * <p> @TableField(fill = FieldFill.UPDATE):在修改时自动插入创建时间</p>
     * <p>@TableField(fill = FieldFill.INSERT_UPDATE):在新增、修改时自动插入创建时间</p>
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 乐观锁
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    /**
     * 逻辑删除,单表配置
     * <p>@TableLogic:表示这张表通过这个字段实现逻辑删除，如果删除该注解，就会直接删除数据</p>
     * <p>但如果实体类上有 @TableLogic 则以实体上的为准，忽略全局。 即先查找注解再查找全局，都没有则此表没有逻辑删除。</p>
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 表示与数据库的字段不进行匹配
     */
    @TableField(exist = false)
    private Integer phoneNumber;
}
