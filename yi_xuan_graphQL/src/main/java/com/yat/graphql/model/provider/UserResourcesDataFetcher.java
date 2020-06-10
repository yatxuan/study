package com.yat.graphql.model.provider;

import com.yat.graphql.model.service.IUserService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserResourcesDataFetcher implements MyDataFetcher {

    private final IUserService userService;


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
