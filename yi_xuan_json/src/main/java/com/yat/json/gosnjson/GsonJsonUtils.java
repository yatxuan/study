package com.yat.json.gosnjson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 17:00
 */
public class GsonJsonUtils {
    /**
     * List ---> Json
     *
     * @param list list集合
     * @return Json字符串
     */
    public static String getListToJson(List list) {

        if (null == list || list.size() < 1) {
            return null;
        }

        return new Gson().toJson(list);
    }

    /**
     * Json ---> List
     *
     * @param listJson Json字符串
     * @return list集合
     */
    public static List getJsonToList(String listJson) {

        //将json格式字符串转换为List对象

        if (StringUtils.isBlank(listJson)) {
            return null;
        }
        return new Gson().fromJson(listJson, List.class);
    }

    /**
     * Json --->  Object
     *
     * @param jsonStr Json字符串
     * @return Object对象
     */
    public static <T> T getJsonToObject(String jsonStr, Class<T> classOfT) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        return new Gson().fromJson(jsonStr, classOfT);
    }

    /**
     * Json --->  Object
     *
     * @param jsonStr Json字符串
     * @return Object对象
     */
    public static JsonObject getJsonToObject(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        return (JsonObject) JsonParser.parseString(jsonStr);
    }


    /**
     * Object ---> Json
     *
     * @param obj Object对象
     * @return Json字符串
     */
    public static String getObjectToJson(Object obj) {
        if (null == obj) {
            return null;
        }
        return new Gson().toJson(obj);
    }

    /**
     * Map ----> Json
     *
     * @param map map对象
     * @return Json字符串
     */
    public static String getMapToJson(Map map) {
        if (null == map) {
            return null;
        }
        return new Gson().toJson(map);
    }

    /**
     * Json ---> Map
     *
     * @param jsonStr Json字符串
     * @return map对象
     */
    public static Map<String, Object> getJsonToMap(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        return new Gson().fromJson(jsonStr, (Type) Map.class);
    }

}
