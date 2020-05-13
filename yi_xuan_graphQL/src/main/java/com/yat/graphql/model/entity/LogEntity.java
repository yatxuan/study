package com.yat.graphql.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/9
 * @Time: 16:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_log")
public class LogEntity {

    @TableId
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private int comment;
}
