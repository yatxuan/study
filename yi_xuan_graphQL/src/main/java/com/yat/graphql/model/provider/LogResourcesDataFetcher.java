package com.yat.graphql.model.provider;

import com.yat.graphql.model.service.ILogService;
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
 * @Time: 17:07
 */
@Component
public class LogResourcesDataFetcher implements MyDataFetcher {

    @Autowired
    private ILogService logService;


    @Override
    public String fieldName() {
        return "logResources";
    }

    @Override
    public Object dataFetcher(DataFetchingEnvironment environment) {
        Long id = environment.getArgument("id");
        return this.logService.getById(id);
    }
}
