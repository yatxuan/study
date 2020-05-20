package com.yat.graphql.model.provider;

import com.yat.graphql.model.service.IUserService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/9
 * @Time: 16:36
 */
@Component
public class UserResourcesDataFetcher implements MyDataFetcher {

    @Autowired
    private IUserService userService;


    @Override
    public String fieldName() {
        return "userResources";
    }

    @Override
    public Object dataFetcher(DataFetchingEnvironment environment) {
        // Long id = environment.getArgument("id");
        // return this.userService.getById(id);
        return this.userService.list();
    }
}