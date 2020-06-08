package com.yat.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.yat.json.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 17:02
 */
public class FastJsonTest {

    public static void main(String[] args) {

        //---------------------List--------------------------------

        // list-json简单类型相互转换
        listStrToJson();
        // list-json对象类型相互转换
        listUserToJson();

        System.out.println();
        //---------------------Object--------------------------------

        // Object-json
        objectToJson();

        System.out.println();
        //---------------------Map--------------------------------

        // Map-json
        mapStrToJson();
        mapObjectToJson();

    }


    private static void listStrToJson() {
        List<String> stringList = new ArrayList<>(3);
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        String json = JSON.toJSONString(stringList);

        List jsonToList =JSON.parseArray(json);
        System.out.println(jsonToList);
    }

    private static void listUserToJson() {

        List<User> stringList = new ArrayList<>(3);
        stringList.add(
                User.builder()
                        .id(1L)
                        .userName("张三")
                        .type("fastJson")
                        .build()
        );
        stringList.add(
                User.builder()
                        .id(2L)
                        .userName("李四")
                        .type("fastJson")
                        .build()
        );
        stringList.add(
                User.builder()
                        .id(3L)
                        .userName("王五")
                        .type("fastJson")
                        .build()
        );
        String json = JSON.toJSONString(stringList);

        List jsonToList =JSON.parseArray(json);
        System.out.println(jsonToList);
    }

    private static void objectToJson() {

        User user = User.builder()
                .id(1L)
                .userName("张三")
                .build();
        String json = JSON.toJSONString(user);
        System.out.println(json);

        User user1 = JSON.parseObject(json, User.class);
        System.out.println(user1);
    }

    private static void mapStrToJson() {

        Map<String, String> stringMap = new HashMap<>(2);
        stringMap.put("1", "张三");
        stringMap.put("2", "李四");

        String json = JSON.toJSONString(stringMap,true);
        System.out.println(json);
        Map<String,String> jsonToMap = (Map<String,String>)JSON.parse(json);
        System.out.println(jsonToMap);
    }

    private static void mapObjectToJson() {

        Map<String, User> stringMap = new HashMap<>(2);
        stringMap.put("1",
                User.builder()
                        .id(1L)
                        .userName("张三")
                        .build()
        );
        stringMap.put("2",
                User.builder()
                        .id(2L)
                        .userName("李四")
                        .build());

        String json = JSON.toJSONString(stringMap,true);
        System.out.println(json);
        Map<String,String> jsonToMap = (Map<String,String>)JSON.parse(json);
        System.out.println(jsonToMap);
    }

}
