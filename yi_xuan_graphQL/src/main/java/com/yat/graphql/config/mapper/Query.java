package com.yat.graphql.config.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yat.graphql.common.constant.SqlConstant;
import com.yat.graphql.config.mapper.xss.SqlFilter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 查询参数
 *
 * @author Yat
 */
public class Query<T> {

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    /**
     *
     * @param params 条件
     * @param defaultOrderField 排序的字段
     * @param isAsc 是否升序排列
     * @return
     */
    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (null != params.get(SqlConstant.PAGE)) {
            curPage = Long.parseLong((String) params.get(SqlConstant.PAGE));
        }
        if (null != params.get(SqlConstant.LIMIT)) {
            limit = Long.parseLong((String) params.get(SqlConstant.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(SqlConstant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SqlFilter.sqlInject((String) params.get(SqlConstant.ORDER_FIELD));
        String order = (String) params.get(SqlConstant.ORDER);

        //前端字段排序
        if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
            if (SqlConstant.ASC.equalsIgnoreCase(order)) {
                return page.setAsc(orderField);
            } else {
                return page.setDesc(orderField);
            }
        }

        //默认排序
        if (isAsc) {
            page.setAsc(defaultOrderField);
        } else {
            page.setDesc(defaultOrderField);
        }

        return page;
    }
}
