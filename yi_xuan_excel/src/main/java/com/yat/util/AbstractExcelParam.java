package com.yat.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: Excel父类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:21
 */
@Data
public abstract class AbstractExcelParam implements Serializable {

    private static final long serialVersionUID = -3943944536174199072L;
    /**
     * 文件地址,本地读取时用
     */
    protected String filePath;

    /**
     * 输出流
     */
    protected HttpServletResponse response;

    /**
     * 文件名
     */
    protected String fileName;

    /**
     * 表头
     */
    protected Boolean fileNameAsHeadName;
    /**
     * 文件输出路径
     */
    protected String outFilePath;

    /**
     * list params
     */
    protected List<ExcelParam> list;


    public String getFileName() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        if (StringUtils.isEmpty(this.fileName)) {
            this.fileName = df.format(new Date());
        }
        return this.fileName;
    }
}
