package com.yat.graphql.model.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.GraphQL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/5/9 17:09
 */
@Controller
@CrossOrigin
@RequestMapping("/graphql")
public class GraphQLController {

    @Autowired
    private GraphQL graphQL;

    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * 实现GraphQL查询
     * String query = "{UserResources{id,name,age}}";
     *
     * @return 、
     */
    @GetMapping
    @ResponseBody
    public Map<String, Object> query(String query) {

        if (StringUtils.equals(query, "1")) {
            query = "{userResources{id,name,age}}";
        }else {
            query = "{logResources(id:1){id,title,content}}";
        }
        return this.graphQL.execute(query).toSpecification();
    }

    @PostMapping
    @ResponseBody
    public Map<String, Object> postQuery(@RequestBody String json) {

        try {
            JsonNode jsonNode = MAPPER.readTree(json);
            if (jsonNode.has("query")) {
                String query = jsonNode.get("query").textValue();
                return this.graphQL.execute(query).toSpecification();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> error = new HashMap<>();
        error.put("status", 500);
        error.put("msg", "查询出错");
        return error;
    }

}
