package com.yat.common.core.text;


import com.yat.common.refactoring.toolkit.StringUtils;

/**
 * <p>Description: 字符串格式化 </p>
 *
 * @author Yat-Xuan
 * @date 2020/3/27 10:57
 */
public class StrFormatter {

    private static final String EMPTY_JSON = "{}";
    private static final char C_BACKSLASH = '\\';
    private static final char C_EMPTY_START = '{';
    public static final char C_EMPTY_END = '}';

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray   参数列表
     * @return 结果
     */
    public static String format(final String strPattern, final Object... argArray) {
        if (StringUtils.isEmpty(strPattern) || StringUtils.isEmpty(argArray)) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();

        // 初始化定义好的长度以获得更好的性能
        StringBuilder builder = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            // 占位符所在位置
            int placeholderIndex = strPattern.indexOf(EMPTY_JSON, handledPosition);
            if (placeholderIndex == -1) {
                if (handledPosition == 0) {
                    return strPattern;
                } else { // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                    builder.append(strPattern, handledPosition, strPatternLength);
                    return builder.toString();
                }
            } else {
                if (placeholderIndex > 0 && strPattern.charAt(placeholderIndex - 1) == C_BACKSLASH) {
                    if (placeholderIndex > 1 && strPattern.charAt(placeholderIndex - 2) == C_BACKSLASH) {
                        // 转义符之前还有一个转义符，占位符依旧有效
                        builder.append(strPattern, handledPosition, placeholderIndex - 1);
                        builder.append(Convert.utf8Str(argArray[argIndex]));
                        handledPosition = placeholderIndex + 2;
                    } else {
                        // 占位符被转义
                        argIndex--;
                        builder.append(strPattern, handledPosition, placeholderIndex - 1);
                        builder.append(C_EMPTY_START);
                        handledPosition = placeholderIndex + 1;
                    }
                } else {
                    // 正常占位符
                    builder.append(strPattern, handledPosition, placeholderIndex);
                    builder.append(Convert.utf8Str(argArray[argIndex]));
                    handledPosition = placeholderIndex + 2;
                }
            }
        }
        // 加入最后一个占位符后所有的字符
        builder.append(strPattern, handledPosition, strPattern.length());

        return builder.toString();
    }
}
