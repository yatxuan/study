package com.yat.config.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yat.common.constant.UserConstant;
import com.yat.config.shiro.jwt.JwtToken;
import com.yat.config.shiro.jwt.JwtUtil;
import com.yat.models.entity.RoleEntity;
import com.yat.models.entity.UserEntity;
import com.yat.models.entity.dto.authority.LoginUser;
import com.yat.models.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: 自定义：登陆、权限认证 </p>
 *
 * @author Yat-Xuan
 * @date 2020/6/20 - 12:56
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShiroRealm extends AuthorizingRealm {

    private final JwtUtil jwtUtil;
    private final IUserService userService;

    /**
     * 必须重写此方法，不然Shiro会报错
     *
     * @param token shiro默认的token
     * @return 、
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * <p>Description: 登录认证 </p>
     *
     * @param token 用于收集用户提交的身份（如用户名）及凭据（如密码）
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @author Yat-Xuan
     * @date 2020/6/20 13:05
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("进入ShiroRealm登陆认证------------------------->");
        // 获取token,这里获取的accessToken
        // 是在自定义的权限验证过滤器:AuthFilter-executeLogin() 中,调用  subject.login(token)放入的数据
        String accessToken = (String) token.getPrincipal();
        // 因为在自定义的ShiroAuthFilter中已经验证了token的真实性，
        // 所以这里就没必要进行二次验证了，这里只验证token是否为空就够了（这是为了避免数据在传输中存在丢失问题）
        if (StringUtils.isBlank(accessToken)) {
            throw new IncorrectCredentialsException("登陆状态已过期，请重新登录");
        }
        // 解析token字符，获取当前登陆的用户信息
        LoginUser loginUser = jwtUtil.getLoginUser(accessToken);

        // 判断当前登陆 在数据库中是否存在
        String username = loginUser.getUsername();

        UserEntity userEntity = userService.getOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUsername, username),
                false
        );

        // 账号锁定
        if (UserConstant.STATUS_DISABLE.equals(userEntity.getEnabled())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // 这里要把 用户的消息，jwt令牌 存入shiro的SecurityManager getAuthorizationInfo
        return new SimpleAuthenticationInfo(userEntity, accessToken, getName());
    }

    /**
     * 授权认证
     *
     * @param principals 、
     * @return 、
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取用户信息
        UserEntity user = (UserEntity) principals.getPrimaryPrincipal();
        // 存储角色集合
        Set<String> roles = new HashSet<>(16);
        // 存储权限集合
        Set<String> permissions = new HashSet<>(16);
        // TODO 这里查询用户角色和权限后，绑定到info里
        System.err.println("查询数据");
        // 查询角色列表 - 没有数据演示，所以现在是默认添加为所有角色
        List<RoleEntity> userRoles = userService.findUserRoles(user.getId());
        roles = userRoles.stream().map(RoleEntity::getRole).collect(Collectors.toSet());
        // roles.add("*");

        // 查询权限列表（功能列表-菜单列表） - 没有数据演示，所以现在是默认添加为所有权限
        // List<PermissionEntity> userPermissions = shiroService.findUserPermissions(user.getId());
        // Set<String> permissions = userPermissions.stream().map(PermissionEntity::getPermission).collect(Collectors.toSet());
        permissions.add("*");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 角色加入AuthorizationInfo认证对象
        info.setRoles(roles);
        // 权限加入AuthorizationInfo认证对象
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 这里是shiro获取的用户权限信息，
     *
     * @param principals 、
     * @return 、
     */
    @Override
    protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        return super.getAuthorizationInfo(principals);
    }


    /**
     * 重写方法,清除当前用户的的 授权缓存
     *
     * @param principals 、
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     *
     * @param principals 、
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    private void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    private void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    /**
     * 自定义方法：清除当前登陆用户的权限
     */
    public void clearUserCache() {
        getAuthorizationCache().remove(SecurityUtils.getSubject().getPrincipal());
        // 清除用户的认证缓存
        getAuthenticationCache().remove(SecurityUtils.getSubject().getPrincipal());
    }
}
