package com.stockmonitor.util;

public class IntegerUtil {
    /**
     * 将字符串转为整数
     * 
     * @param param //需要转换的字符串
     * @return Integer
     * @author ylq Created at 2018-10-16
     */
    public static Integer parse(String param) {
        if (StringUtil.isBlank(param)) {
            return null;
        }

        try {
            return Integer.parseInt(param);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 两数相减
     * 
     * @param leftOperand  //左操作数
     * @param rightOperand //右操作数
     * @return Long
     */
    public static Integer subNotNull(Integer leftOperand, Integer rightOperand) {
        if (leftOperand == null || rightOperand == null) {
            return null;
        }
        return leftOperand - rightOperand;
    }
}
