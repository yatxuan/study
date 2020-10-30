package com.yat.model;

import com.yat.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/10/29 - 15:30
 */
@Data
public class Temporary implements Serializable {

    private static final long serialVersionUID = -8714467561214491584L;

    @Excel(title = "字段名称举例")
    private String a;
    @Excel(title = "说明")
    private String b;
    @Excel(title = "字段值举例")
    private String c;
    @Excel(title = "含义")
    private String d;
    @Excel(title = "返回值类型")
    private String e;



}
