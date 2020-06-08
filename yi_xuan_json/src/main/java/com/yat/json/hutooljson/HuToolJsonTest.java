package com.yat.json.hutooljson;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yat.json.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 17:19
 */
public class HuToolJsonTest {


    public static void main(String[] args) {

        //---------------------Object--------------------------------

        // Object-json
        objectToJson();

        System.out.println();
        //---------------------Map--------------------------------

        // Map-json
        mapStrToJson();
        mapObjectToJson();

    }

    private static void objectToJson() {
        User user = User.builder()
                .id(1L)
                .userName("张三")
                .type("huToolJson")
                .build();

        JSON parse = JSONUtil.parseObj(user, false);
        System.out.println(parse.toStringPretty());

        JSONObject jsonObject = JSONUtil.parseObj(parse.toStringPretty());
        System.out.println(jsonObject);
    }

    private static void mapStrToJson() {

        Map<String, String> stringMap = new HashMap<>(2);
        stringMap.put("1", "张三");
        stringMap.put("2", "李四");

        JSONObject jsonObject = JSONUtil.parseObj(stringMap);
        String str = jsonObject.toStringPretty();
        System.out.println(str);

    }

    private static void mapObjectToJson() {

        Map<String, User> stringMap = new HashMap<>(2);
        stringMap.put("1",
                User.builder()
                        .id(1L)
                        .userName("张三")
                        .type("huToolJson")
                        .build()
        );
        stringMap.put("2",
                User.builder()
                        .id(2L)
                        .userName("李四")
                        .type("huToolJson")
                        .build());

        JSONObject jsonObject = JSONUtil.parseObj(stringMap);
        String str = jsonObject.toStringPretty();
        System.out.println(str);

    }

}

