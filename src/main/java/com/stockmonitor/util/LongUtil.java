package com.stockmonitor.util;

public class LongUtil {

    /**
     * String转Long
     * 
     * @param param 需要转换的字符串
     * @return Long
     */
    public static Long parse(String param, Long defaultNumber) {
        if (StringUtil.isBlank(param)) {
            return defaultNumber;
        }

        try {
            Long num = Long.parseLong(param);
            return num;
        } catch (Exception e) {

        }
        return defaultNumber;
    }

    /**
     * String转Long
     * 
     * @param param 需要转换的字符串
     * @return Long
     */
    public static Long parse(String param) {
        if (StringUtil.isBlank(param)) {
            return null;
        }

        try {
            Long num = Long.parseLong(param);
            return num;
        } catch (Exception e) {

        }
        return null;
    }

    public static String formatData(Long data) {
        return formatData(data, "-");
    }

    public static String formatData(Long data, String defaultValue) {
        return data == null ? defaultValue : data.toString();
    }

    /**
     * 两数相加
     * 
     * @param leftOperand  //左操作数
     * @param rightOperand //右操作数
     * @return Long
     */
    public static Long add(Long leftOperand, Long rightOperand) {
        if (leftOperand == null) {
            return rightOperand;
        }
        if (rightOperand == null) {
            return leftOperand;
        }

        return leftOperand + rightOperand;
    }

    /**
     * 两数相乘
     * 
     * @param leftOperand  //左操作数
     * @param rightOperand //右操作数
     * @return Long
     */
    public static Long multiply(Long leftOperand, Long rightOperand) {
        if (leftOperand == null || rightOperand == null) {
            return null;
        }

        return leftOperand * rightOperand;
    }

    /**
     * 两数相减
     * 
     * @param leftOperand  //左操作数
     * @param rightOperand //右操作数
     * @return Long
     */
    public static Long sub(Long leftOperand, Long rightOperand) {
        if (leftOperand == null) {
            if (rightOperand == null) {
                return null;
            }
            return 0 - rightOperand;

        } else if (rightOperand == null) {
            return leftOperand;
        }

        return leftOperand - rightOperand;
    }

    /**
     * 两数相减
     * 
     * @param leftOperand  //左操作数
     * @param rightOperand //右操作数
     * @return Long
     */
    public static Long subNotNull(Long leftOperand, Long rightOperand) {
        if (leftOperand == null || rightOperand == null) {
            return null;
        }
        return leftOperand - rightOperand;
    }

    /**
     * 返回较大值
     * 
     * @param leftOperand
     * @param rightOperand
     * @return BigDecimal
     */
    public static Long max(Long leftOperand, Long rightOperand) {
        if (leftOperand == null) {
            return rightOperand;
        }
        if (rightOperand == null) {
            return leftOperand;
        }

        return leftOperand.compareTo(rightOperand) > 0 ? leftOperand : rightOperand;
    }

    /**
     * 返回较小值
     * 
     * @param leftOperand
     * @param rightOperand
     * @return BigDecimal
     */
    public static Long min(Long leftOperand, Long rightOperand) {
        if (leftOperand == null) {
            return rightOperand;
        }
        if (rightOperand == null) {
            return leftOperand;
        }

        return leftOperand.compareTo(rightOperand) < 0 ? leftOperand : rightOperand;
    }

    public static Double formatToDouble(Long number, Double defaultNumber) {
        return number == null ? defaultNumber : number.doubleValue();
    }

}
