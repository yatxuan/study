package com.yat.reptile.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证码
 *
 * @author Yat-Xuan
 * @since 2019/7/16 11:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyImageDTO implements Serializable {

    private static final long serialVersionUID = 6741944800448697513L;

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
    /**
     * 验证码的值
     */
    private String result;

}
