package com.yat.modules.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yat.modules.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 权限表
 *
 * @author 熊能
 * @version 1.0
 * @since 2018/01/02
 */@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tb_permission")
public class PermissionEntity extends BaseEntity {

    /**
     * 权限名称
     */
    private String permission;
    /**
     * 权限说明
     */
    private String description;

}
