package com.yat.graphql.model.provider;

import graphql.schema.DataFetchingEnvironment;

/**
 * <p>Description: 描述 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/9
 * @Time: 16:34
 */
public interface MyDataFetcher {

    /**
     * 查询名称
     *
     * @return \
     */
    String fieldName();

    /**
     * 具体实现数据查询的逻辑
     *
     * @param environment \
     * @return \
     */
    Object dataFetcher(DataFetchingEnvironment environment);
}
