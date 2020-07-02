package com.yat.models.entity.dto.authority;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>Description: 存储登陆的用户信息 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 13:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 当前用户登陆的ip
     */
    private String logIp;
    /**
     * 登陆的浏览器名称
     */
    private String browser;
    /**
     * 登录地点
     */
    private String address;
    /**
     * 登陆时间
     */
    private String loginTime;
    /**
     * 设置每个账号最多同时几个客户端登录：允许最大的登陆人数
     */
    private Integer logNumber;
    /**
     *1-挤调第一个地方的登陆，0-不挤掉，-1-返回报错提示
     */
    private Integer squeeze;
}
