package com.yat.json.gosnjson;

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
public class GsonJsonTest {

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
        String listToJson = GsonJsonUtils.getListToJson(stringList);
        System.out.println(listToJson);

        List jsonToList = GsonJsonUtils.getJsonToList(listToJson);
        System.out.println(jsonToList);
    }

    private static void listUserToJson() {

        List<User> stringList = new ArrayList<>(3);
        stringList.add(
                User.builder()
                        .id(1L)
                        .userName("张三")
                        .build()
        );
        stringList.add(
                User.builder()
                        .id(2L)
                        .userName("李四")
                        .build()
        );
        stringList.add(
                User.builder()
                        .id(3L)
                        .userName("王五")
                        .build()
        );
        String listToJson = GsonJsonUtils.getListToJson(stringList);
        System.out.println(listToJson);

        List jsonToList = GsonJsonUtils.getJsonToList(listToJson);
        System.out.println(jsonToList);
    }

    private static void objectToJson() {

        User user = User.builder()
                .id(1L)
                .userName("张三")
                .build();
        String userJson = GsonJsonUtils.getObjectToJson(user);
        System.out.println(userJson);

        User jsonToObject = GsonJsonUtils.getJsonToObject(userJson, User.class);
        System.out.println(jsonToObject);
    }

    private static void mapStrToJson() {

        Map<String, String> stringMap = new HashMap<>(2);
        stringMap.put("1", "张三");
        stringMap.put("2", "李四");

        String mapToJson = GsonJsonUtils.getMapToJson(stringMap);
        System.out.println(mapToJson);

        Map<String, Object> jsonToMap = GsonJsonUtils.getJsonToMap(mapToJson);
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

        String mapToJson = GsonJsonUtils.getMapToJson(stringMap);
        System.out.println(mapToJson);

        Map<String, Object> jsonToMap = GsonJsonUtils.getJsonToMap(mapToJson);
        System.out.println(jsonToMap);
    }

}
