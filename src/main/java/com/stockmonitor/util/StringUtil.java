package com.stockmonitor.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     * 
     * @param str 字符串
     * @return true:为空 false:不为空
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否不为空
     * 
     * @param str 字符串
     * @return true:不为空 false:为空
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    /**
     * 判断字符串是否为空白
     * 
     * @param str 字符串
     * @return true:为空白 false:不为空白
     */
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * 判断字符串是否不为空白
     * 
     * @param str 字符串
     * @return true:不为空白 false:为空白
     */
    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    /**
     * 去除字符串两端的空白字符
     * 
     * @param str 字符串
     * @return 处理后的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 将字符串转换为大写
     * 
     * @param str 字符串
     * @return 转换后的字符串
     */
    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    /**
     * 将字符串转换为小写
     * 
     * @param str 字符串
     * @return 转换后的字符串
     */
    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    /**
     * 截取字符串
     * 
     * @param str   字符串
     * @param start 开始位置
     * @param end   结束位置
     * @return 截取后的字符串
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    public static String[] parseList(Set<String> set) {
        String[] list = new String[set.size()];
        set.toArray(list);
        return list;

    }
}
