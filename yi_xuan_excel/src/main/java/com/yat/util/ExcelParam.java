package com.yat.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExcelParam extends AbstractExcelParam implements Serializable {

    private static final long serialVersionUID = -4231868339831975335L;

    /**
     * 反射具体类
     */
    private Class clazz;
    /**
     * 从第几行开始扫描
     */
    private Integer rowNumIndex;
    /**
     * 读取到第几个sheet结束
     */
    private Integer sheetIndex;
    /**
     * 读取指定的sheetName或写入指定的sheetName
     */
    private String sheetName;
    /**
     * 存储属性和表头的对应关系
     */
    private Map map;
    /**
     * keyValue
     */
    private String keyValue;
    /**
     * 表头是否强一致
     */
    private Boolean sameHeader = false;
    /**
     * 是否流读取
     */
    private Boolean stream = false;
    /**
     * 用流代替本地文件
     */
    private byte[] buf;

    /**
     * 表头
     */
    private String headerName;

    /**
     * 文件导出封装数据
     */
    private List list;
    /**
     * 文件导出封装数据
     */
    private Object obj;

    /**
     * 水印文字
     */
    private String waterMark;

    public ExcelParam(Class clazz, String keyValue, String outFilePath, List list) {
        this.clazz = clazz;
        this.keyValue = keyValue;
        this.outFilePath = outFilePath;
        this.list = list;
    }

    public ExcelParam(Class clazz, String outFilePath, List list) {
        this.clazz = clazz;
        this.outFilePath = outFilePath;
        this.list = list;
    }

    public ExcelParam(Class clazz, String outFilePath, List list, String headerName) {
        this.clazz = clazz;
        this.outFilePath = outFilePath;
        this.list = list;
        this.headerName = headerName;
    }

    public ExcelParam(Class clazz, HttpServletResponse response, List list) {
        this.clazz = clazz;
        this.response = response;
        this.list = list;
    }

    public ExcelParam(Class clazz, HttpServletResponse response, List list, String headerName) {
        this.clazz = clazz;
        this.response = response;
        this.list = list;
        this.headerName = headerName;
    }

    public ExcelParam(Class clazz, String keyValue, HttpServletResponse response, List list) {
        this.clazz = clazz;
        this.keyValue = keyValue;
        this.response = response;
        this.list = list;
    }

    public ExcelParam(Class clazz, String keyValue, HttpServletResponse response, String fileName, List list) {
        this.clazz = clazz;
        this.keyValue = keyValue;
        this.response = response;
        this.fileName = fileName;
        this.headerName = fileName;
        this.list = list;
    }

    public ExcelParam(Class clazz, String keyValue, HttpServletResponse response, String fileName, Boolean fileNameAsHeaderName, List list) {
        this.clazz = clazz;
        this.keyValue = keyValue;
        this.response = response;
        this.fileName = fileName;
        if (fileNameAsHeaderName) {
            this.headerName = fileName;
        }
        this.list = list;
    }

    public ExcelParam(Class clazz, HttpServletResponse response, String fileName, List list) {
        this.clazz = clazz;
        this.response = response;
        this.fileName = fileName;
        this.list = list;
    }

    public ExcelParam(Class clazz, HttpServletResponse response, String fileName, Boolean fileNameAsHeaderName, List list) {
        this.clazz = clazz;
        this.response = response;
        this.fileName = fileName;
        if (fileNameAsHeaderName) {
            this.headerName = fileName;
        }
        this.list = list;
    }

    public ExcelParam(HttpServletResponse response, String templatePath, String outFilePath, Object obj) {
        this.response = response;
        this.filePath = templatePath;
        this.outFilePath = outFilePath;
        this.obj = obj;
    }

    public ExcelParam(HttpServletResponse response, String templatePath, Object obj, String fileName) {
        this.response = response;
        this.filePath = templatePath;
        this.fileName = fileName;
        this.obj = obj;
    }

}
