package com.yat.mariadb.modules.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.yat.mariadb.common.constant.SqlConstant;
import com.yat.mariadb.modules.entity.UserEntity;
import com.yat.mariadb.modules.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 16:05
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/list")
    public IPage<UserEntity> getUserList() {
        Map<String, Object> params = new HashMap<>(5);

        // 页码
        params.put(SqlConstant.PAGE, "1");
        // 每页条数
        params.put(SqlConstant.LIMIT, "10");
        // 前端排序字段
        params.put(SqlConstant.ORDER_FIELD, "age");
        // 前端排序方式
        params.put(SqlConstant.ORDER, SqlConstant.ASC);

        return userService.selectUserPage(params);
    }

    @GetMapping("/update")
    public UserEntity Update() {
        UserEntity byId = userService.getById("1263001984569470977");
        byId.setAge(20);

        userService.updateById(byId);
        return byId;
    }


    @GetMapping("/save")
    public UserEntity save() {
        String idStr = IdWorker.getIdStr();

        UserEntity userEntity = new UserEntity();
        userEntity.setName("name" + idStr);
        userEntity.setAge(0);
        userEntity.setEmail("email" + idStr);
        userEntity.setId(idStr);

        userService.save(userEntity);
        return userEntity;
    }
}
