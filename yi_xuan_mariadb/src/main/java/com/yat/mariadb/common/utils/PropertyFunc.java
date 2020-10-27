package com.yat.mariadb.common.utils;

import java.io.Serializable;
import java.util.function.Function;

/**
 * <p>Description: 通过自定义类，实现lambda解析类字段 </p>
 *
 * @author Yat-Xuan
 * @date 2020/10/26 16:55
 */
public interface PropertyFunc<T, R> extends Function<T, R>, Serializable {

}
