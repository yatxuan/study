package com.yat.grammar;

import com.yat.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/7/27 - 16:31
 */
public class Demo1 {

    public static void main(String[] args) {
        List<UserEntity> list = new ArrayList<>(16);
        list.add(new UserEntity("admin", "admin"));
        list.add(new UserEntity("user", "user"));
        list.add(new UserEntity(null, "test"));


        // 下面的作用：把list集合中的用户姓名提取出来，作为一个新的集合
        // filter : 条件判断
        List<String> userList = list.stream()
                .filter(userEntity -> userEntity.getUsername() != null)
                .map(UserEntity::getUsername)
                .collect(Collectors.toList());
        userList.forEach(System.out::println);

        System.out.println("---------------------------------");

        List<String> userList2 = list.stream()
                .filter(userEntity -> userEntity.getUsername() != null)
                .map(userEntity -> userEntity.getUsername())
                .collect(Collectors.toList());
        userList2.forEach(user -> System.out.println(user));

    }
}
