package com.yat.json.jsoncode;

import cn.miludeer.jsoncode.JsonCode;

import java.util.Arrays;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/6/8
 * @Time: 17:38
 */
public class JsonCodeUtils {

    private final static String JSON = "{" +
            "    \"json\": {" +
            "        \"a\": {" +
            "            \"www\": \"ff\"," +
            "            \"rrr\": [\"v1\", \"v2\"]" +
            "        }," +
            "        \"b\": {" +
            "            \"www\": \"4567ttt\"," +
            "            \"rrr\": [\"v3\", \"v4\"]" +
            "        }" +
            "    }" +
            "}";

    public static void main(String[] argv) {
        // 直接取值
        String value = JsonCode.getValue(JSON, "$.json.b.www");
        System.out.println(value);

        // 如果key对应的value是list集合类型，也可以这么取数据
        String[] valueList = JsonCode.getValueList(JSON, "$.json.b.rrr");
        Arrays.stream(valueList).forEach(System.out::println);
    }
}
