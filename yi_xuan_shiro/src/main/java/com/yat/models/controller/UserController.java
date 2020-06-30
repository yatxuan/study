package com.yat.models.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yat.common.utils.ResultResponse;
import com.yat.config.redis.RedisUtils;
import com.yat.config.shiro.realm.ShiroRealm;
import com.yat.models.entity.UserEntity;
import com.yat.models.entity.UserRolesEntity;
import com.yat.models.service.IUserRoleService;
import com.yat.models.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import static com.yat.common.constant.CommonConstant.*;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/6/24 - 10:01
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IUserRoleService userRoleService;
    private final RedisUtils redisUtils;


    @GetMapping("/{id}")
    public ResultResponse get(@PathVariable("id") long id) {
        UserEntity userEntity = userService.getById(id);

        return ResultResponse.success(userEntity);
    }

    @PutMapping("/{id}")
    public ResultResponse update(@PathVariable("id") long id) {

        boolean update = userRoleService.update(
                new LambdaUpdateWrapper<UserRolesEntity>()
                        .set(UserRolesEntity::getRoleId, 2)
                        .eq(UserRolesEntity::getUserId, id)
        );
        UserEntity userEntity = userService.getById(id);

        // 删除缓存的用户信息和权限
        redisUtils.del(PREFIX_SHIRO_CACHE + AUTHORIZATION_CACHE + userEntity.getUsername(), PREFIX_SHIRO_CACHE + AUTHENTICATION_CACHE + userEntity.getUsername());

        return ResultResponse.success(update);
    }

    /**
     * @param principal
     * @title 刷新用户权限
     * @desc principal为用户的认证信息
     */
    public static void reloadAuthorizing(Object principal) throws Exception {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        ShiroRealm myShiroRealm = (ShiroRealm) rsm.getRealms().iterator().next();

        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, realmName);
        subject.runAs(principals);
        if (myShiroRealm.isAuthenticationCachingEnabled()) {
            myShiroRealm.getAuthenticationCache().remove(principals);
        }
        if (myShiroRealm.isAuthorizationCachingEnabled()) {
            // 删除指定用户shiro权限
            myShiroRealm.getAuthorizationCache().remove(principals);
        }
        // 刷新权限
        subject.releaseRunAs();
    }


    /**
     * 重新赋值权限(在比如:给一个角色临时添加一个权限,需要调用此方法刷新权限,否则还是没有刚赋值的权限)
     *
     * @param myRealm  自定义的realm
     * @param username 用户名
     */
    public static void reloadAuthorizing(ShiroRealm myRealm, String username) {
        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        //第一个参数为用户名,第二个参数为realmName,test想要操作权限的用户
        SimplePrincipalCollection principals = new SimplePrincipalCollection(username, realmName);
        subject.runAs(principals);
        myRealm.getAuthorizationCache().remove(subject.getPrincipals());
        subject.releaseRunAs();
    }
}
