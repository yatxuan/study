package com.yat.common;

import cn.hutool.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>Description: 操作消息提醒 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/24 14:23
 */
@SuppressWarnings("all")
public class ResultResponse extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    private static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    private static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 ResultResponse 对象，使其表示一个空消息。
     */
    public ResultResponse() {
    }

    /**
     * 初始化一个新创建的 ResultResponse 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public ResultResponse(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 ResultResponse 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public ResultResponse(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (null == data) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ResultResponse success() {
        return ResultResponse.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static ResultResponse success(Object data) {
        return ResultResponse.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ResultResponse success(String msg) {
        return ResultResponse.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ResultResponse success(String msg, Object data) {
        return new ResultResponse(HttpStatus.HTTP_OK, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return 、
     */
    public static ResultResponse error() {
        return ResultResponse.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResultResponse error(String msg) {
        return ResultResponse.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ResultResponse error(String msg, Object data) {
        return new ResultResponse(HttpStatus.HTTP_INTERNAL_ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static ResultResponse error(int code, String msg) {
        return new ResultResponse(code, msg, null);
    }
}
