package com.yat.models.entity.from;

import lombok.Data;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/31
 * @Time: 11:36
 */
@Data
public class LoginFrom {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码的UUID
     */
    private String uuid;
    /**
     * 验证码的值
     */
    private String code;

    /**
     * <p>是否挤出前一个登陆的用户 ：</p>
     * <p>1-挤调第一个地方的登陆，0-不挤掉，-1-返回报错提示</p>
     */
    private int squeeze = -1;
}
