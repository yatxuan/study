package com.yat.json.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 17:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    private String userName;
    private String type;
}
