package com.yat.common.constant;

/**
 * <p>Description: 用户常量 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/3/30
 * @Time: 16:39
 */
public interface UserConstant {

    /**
     * 用户默认头像
     */
    String USER_DEFAULT_AVATAR = "https://i.loli.net/2019/04/28/5cc5a71a6e3b6.png";
    /**
     * 平台内系统用户的唯一标志
     */
    String SYS_USER = "SYS_USER";
    /**
     * 用户正常状态
     */
    Integer USER_STATUS_NORMAL = 0;

    /**
     * 用户禁用状态
     */
    Integer USER_STATUS_LOCK = -1;

    /**
     * 普通用户
     */
    Integer USER_TYPE_NORMAL = 0;

    /**
     * 管理员
     */
    Long USER_TYPE_ADMIN = 1L;

    /**
     * 角色封禁状态
     */
    String ROLE_BLOCKED = "1";

    /**
     * 部门正常状态
     */
    String DEPT_NORMAL = "0";

    /**
     * 字典正常状态
     */
    String DICT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    String YES = "Y";

    /**
     * 校验返回结果码: 0
     */
    String UNIQUE = "0";
    /**
     * 校验返回结果码: 1
     */
    String NOT_UNIQUE = "1";

    /**
     * 本部门及以下
     */
    String DATA_TYPE_UNDER = "本部门及以下";

    /**
     * 本部门
     */
    String DATA_TYPE_SAME = "本级";

    /**
     * 正常状态
     */
    Integer STATUS_NORMAL = 0;

    /**
     * 禁用状态
     */
    Integer STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    Integer DEL_FLAG = -1;

}
