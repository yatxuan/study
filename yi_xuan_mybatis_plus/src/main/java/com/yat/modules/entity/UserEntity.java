package com.yat.modules.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>Description: 测试表 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/20
 * @Time: 10:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class UserEntity {

    /**
     * <p>@TableId(type = IdType.ID_WORKER):自定义注解自增方式为，全局唯一ID (idWorker)</p>
     * <p>如果在yaml配置文件中配置了，这里就不用配置了</p>
     * <p>如果实体类配置了自增策略，默认以实体类里配置的为主</p>
     */
    @TableId
    private Long id;

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
     * 逻辑删除
     * <p>@TableLogic:表示这张表通过这个字段实现逻辑删除，如果删除该注解，就会直接删除数据</p>
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
