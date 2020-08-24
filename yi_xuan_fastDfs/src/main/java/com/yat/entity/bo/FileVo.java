package com.yat.entity.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/8/24 - 19:21
 */
@Data
public class FileVo implements Serializable {
    private static final long serialVersionUID = 9091884514794053452L;

    /**
     * 图标的base64数据
     */
    private String imageBase;
    /**
     * 是否生成略缩图
     */
    private boolean isThumbnails = false;
}
