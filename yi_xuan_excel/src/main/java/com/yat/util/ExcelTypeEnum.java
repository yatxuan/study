package com.yat.util;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:22
 */
public enum ExcelTypeEnum {

    /**
     * 03版Excel
     */
    EXCEL_THREE(1, "xls"),
    /**
     * 07版Excel
     */
    EXCEL_SEVEN(2, "xlsx");


    private Integer key;

    private String text;

    ExcelTypeEnum(Integer key, String text) {
        this.key = key;
        this.text = text;
    }

    public Integer getKey() {
        return key;
    }


    public String getText() {
        return text;
    }

    public static ExcelTypeEnum getText(Integer key) {
        for (ExcelTypeEnum typeEnum : ExcelTypeEnum.values()) {
            if (typeEnum.key.equals(key)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("No element matches " + key);
    }


}
