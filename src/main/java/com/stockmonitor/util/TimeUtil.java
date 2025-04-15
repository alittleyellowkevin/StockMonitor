package com.stockmonitor.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具类
 */
public class TimeUtil {

    /**
     * 获取当前GMT时间
     * 
     * @return GMT格式的时间字符串,如: Wed, 27 Dec 2023 12:34:56 GMT
     */
    public static String getCurrentGMTTime() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(cd.getTime());
    }

    /**
     * 将Date转换为指定格式的字符串
     * 
     * @param date    日期
     * @param pattern 格式,如yyyy-MM-dd HH:mm:ss
     * @return 格式化后的时间字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 将字符串解析为Date
     * 
     * @param dateStr 时间字符串
     * @param pattern 格式,如yyyy-MM-dd HH:mm:ss
     * @return Date对象
     */
    public static Date parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前时间
     * 
     * @return 当前时间
     */
    public static Date getCurrentTime() {
        return new Date();
    }

    /**
     * 获取当前时间戳
     * 
     * @return 当前时间的毫秒数
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间戳(秒)
     * 
     * @return 当前时间的秒数
     */
    public static long getCurrentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 将Date转换为时间戳
     * 
     * @param date 日期
     * @return 时间戳(毫秒)
     */
    public static long getTimeMillis(Date date) {
        return date == null ? 0L : date.getTime();
    }

    /**
     * 将Date转换为时间戳(秒)
     * 
     * @param date 日期
     * @return 时间戳(秒)
     */
    public static long getTimeSeconds(Date date) {
        return date == null ? 0L : date.getTime() / 1000;
    }

    /**
     * 将时间戳(毫秒)转换为Date
     * 
     * @param timeMillis 时间戳(毫秒)
     * @return Date对象
     */
    public static Date getDateFromMillis(long timeMillis) {
        return new Date(timeMillis);
    }

    /**
     * 将时间戳(秒)转换为Date
     * 
     * @param timeSeconds 时间戳(秒)
     * @return Date对象
     */
    public static Date getDateFromSeconds(long timeSeconds) {
        return new Date(timeSeconds * 1000);
    }

    public static String format(Long time, String pattern, String timeZone) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf.format(new Date(time));
    }

    public static String format(Long time, String pattern) {
        return format(time, pattern, "Asia/Shanghai");
    }
}