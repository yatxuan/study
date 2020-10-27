package com.yat.mariadb.config.mybatis;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yat.mariadb.common.constant.SqlConstant;
import com.yat.mariadb.common.utils.PropertyFunc;
import com.yat.mariadb.config.mybatis.xss.SqlFilter;
import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>Description: 查询参数 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 16:20
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Query<T> {

    /**
     * 分页查询(不添加默认排序)
     */
    public Page<T> getPage(Map<String, Object> params) {
        // 分页参数
        long curPage = 1;
        long limit = 10;

        if (null != params.get(SqlConstant.PAGE)) {
            curPage = Long.parseLong(String.valueOf(params.get(SqlConstant.PAGE)));
        }
        if (null != params.get(SqlConstant.LIMIT)) {
            limit = Long.parseLong(String.valueOf(params.get(SqlConstant.LIMIT)));
        }

        // 分页对象
        Page<T> page = new Page<>(curPage, limit);

        // 分页参数
        params.put(SqlConstant.PAGE, page);

        // 获取排序字段
        String orderField = SqlFilter.sqlInject((String) params.get(SqlConstant.ORDER_FIELD));
        // 获取排序方式
        String order = (String) params.get(SqlConstant.ORDER);

        // 前端字段排序
        if (StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(order)) {
            if (StringUtils.equalsIgnoreCase(SqlConstant.ASC, order)) {
                return page.addOrder(OrderItem.asc(orderField));
            } else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        return page;
    }

    /**
     * 分页查询(设置默认排序)
     *
     * @param params 条件
     * @param func   默认排序字段
     * @param isAsc  默认排序方式
     * @return 、
     */
    public Page<T> getPage(Map<String, Object> params, PropertyFunc<T, ?> func, boolean isAsc) {

        // 分页对象
        Page<T> page = getPage(params);

        // 默认排序
        if (null != func) {
            String defaultOrderField = getFieldName(func);
            if (isAsc && StringUtils.isNotBlank(defaultOrderField)) {
                page.addOrder(OrderItem.asc(defaultOrderField));
            } else {
                page.addOrder(OrderItem.desc(defaultOrderField));
            }
        }

        return page;
    }

    /**
     * 解析Lambda获取类属性字段
     *
     * @param func 、
     * @param <T>  、
     * @return 、
     */
    private static <T> String getFieldName(PropertyFunc<T, ?> func) {
        try {
            // 通过获取对象方法，判断是否存在该方法
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            // 利用jdk的SerializedLambda 解析方法引用
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(func);
            String getter = serializedLambda.getImplMethodName();
            return resolveFieldName(getter);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static String resolveFieldName(String getMethodName) {

        final String strGet = "get";
        final String strIs = "is";

        if (getMethodName.startsWith(strGet)) {
            getMethodName = getMethodName.substring(3);
        } else if (getMethodName.startsWith(strIs)) {
            getMethodName = getMethodName.substring(2);
        }
        // 小写第一个字母
        if (StringUtils.isBlank(getMethodName)) {
            return "";
        }
        return getMethodName.substring(0, 1).toLowerCase() + getMethodName.substring(1);
    }

}
