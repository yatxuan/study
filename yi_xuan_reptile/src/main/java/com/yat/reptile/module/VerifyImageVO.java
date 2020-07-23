package com.yat.reptile.module;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Yat-Xuan
 * @since 2019/7/16 14:58
 */
@Data
public class VerifyImageVO implements Serializable {

    private static final long serialVersionUID = 345634706484343777L;

    /**
     * 验证码的UUID
     */
    private String verifyId;
    /**
     * 验证码类型
     */
    private String verifyType;
    /**
     * 验证码的base64-图片
     */
    private String verifyImgStr;

}
