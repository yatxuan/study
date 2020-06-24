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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

}
