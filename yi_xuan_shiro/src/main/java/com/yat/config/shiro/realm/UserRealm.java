package com.yat.config.shiro.realm;

import com.yat.common.constant.UserConstant;
import com.yat.common.utils.StringUtils;
import com.yat.config.shiro.jwt.JwtToken;
import com.yat.config.shiro.jwt.JwtUtil;
import com.yat.modules.authority.dto.LoginUser;
import com.yat.modules.authority.service.ShiroService;
import com.yat.modules.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


/**
 * <p>Description: 登陆、权限认证 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2019/9/10
 * @Time: 11:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRealm extends AuthorizingRealm {

    private final JwtUtil jwtUtil;
    private final ShiroService shiroService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取用户信息
        UserEntity user = (UserEntity) principals.getPrimaryPrincipal();

        // TODO 这里查询用户角色和权限后，绑定到info里

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 查询角色列表
        Set<String> roles = new HashSet<>(16);
        roles.add("*");
        // 查询功能列表
        Set<String> menus = new HashSet<>(16);
        menus.add("*");
        // 角色加入AuthorizationInfo认证对象
        info.setRoles(roles);
        // 权限加入AuthorizationInfo认证对象
        info.setStringPermissions(menus);
        return info;
    }

    /**
     * 登录认证,虽然之前已经验证了token一次，但是为了以防万一，在这里还是需要做一次token，双重认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 根据accessToken，查询用户信息
        String accessToken = (String) token.getPrincipal();
        //token失效
        if (StringUtils.isBlank(accessToken)) {
            throw new IncorrectCredentialsException("登陆状态已过期，请重新登录");
        }

        // 解析token字符，获取当前登陆的用户信息
        LoginUser loginUser = jwtUtil.getLoginUser(accessToken);

        // 判断当前登陆 在数据库中是否存在
        String username = loginUser.getUsername();
        // 查询用户信息
        UserEntity userEntity = shiroService.findUserByUserName(username);
        // 清除用户密码，不存入shiro中的任何地方
        userEntity.setPassword("");
        // 账号锁定
        if (UserConstant.STATUS_DISABLE.equals(userEntity.getEnabled())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // 这里要把 用户的消息，jwt令牌 存入shiro的SecurityManager
        return new SimpleAuthenticationInfo(userEntity, accessToken, getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
