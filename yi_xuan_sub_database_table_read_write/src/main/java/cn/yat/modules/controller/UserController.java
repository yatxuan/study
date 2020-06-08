package cn.yat.modules.controller;

import cn.yat.modules.common.id.IdWorker;
import cn.yat.modules.entity.UserEntity;
import cn.yat.modules.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/4/28
 * @Time: 14:29
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final IUserService userService;
    private final IdWorker idWorker;

    @GetMapping
    public List<UserEntity> getUser() {
        return userService.list();
    }

    @PostMapping
    public String setUser() {
        UserEntity userEntity = new UserEntity();

        for (int i = 0; i < 5; i++) {
            userEntity.setId(idWorker.nextId());
            userEntity.setName("name-" + i);
            userEntity.setPassword("password-" + i);
            userService.save(userEntity);
        }

        return "success";
    }


    @PutMapping
    public UserEntity putUser() {
        List<UserEntity> list = userService.list();
        UserEntity userEntity = list.get(0);
        userService.update(
                new UpdateWrapper<UserEntity>().lambda()
                        .set(UserEntity::getPassword, userEntity.getPassword() + "000")
                        .eq(UserEntity::getId, userEntity.getId())
        );
        return userService.getById(userEntity.getId());

    }

    @DeleteMapping
    public String delUser() {

        List<UserEntity> list = userService.list();
        UserEntity userEntity = list.get(0);
        userService.removeById(userEntity.getId());

        return "success";
    }

}
