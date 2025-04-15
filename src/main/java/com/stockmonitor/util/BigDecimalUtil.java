package com.stockmonitor.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {
    /**
     * 将字符串转为BigDecimal
     * 
     * @param param //需要转换的字符串
     * @return BigDecimal
     * @author ylq Created at 2018-10-16
     */
    public static BigDecimal parse(String param) {
        if (StringUtil.isBlank(param)) {
            return null;
        }

        try {
            return new BigDecimal(param);
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal parse(Long param) {
        if (param == null) {
            return null;
        }

        try {
            return new BigDecimal(param);
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal parse(Integer param) {
        if (param == null) {
            return null;
        }

        try {
            return new BigDecimal(param);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 数据格式化
     * 
     * @param data
     * @return
     */
    public static String formatData(BigDecimal data, String defaultValue, Integer decimal) {
        if (data == null) {
            return defaultValue;
        }
        if (decimal != null) {
            data = data.setScale(decimal, RoundingMode.HALF_UP);
        }
        return data.toPlainString();
    }

    public static String formatData(BigDecimal data) {
        return formatData(data, "-", null);
    }

    public static String formatData(BigDecimal data, String defaultValue) {
        return formatData(data, defaultValue, null);
    }

    public static String formatData(BigDecimal data, Integer decimal) {
        return formatData(data, "-", decimal);
    }

    public static String formatDataAndStripZeros(BigDecimal data, String defaultValue, Integer decimal) {
        if (data == null) {
            return defaultValue;
        }
        if (decimal != null) {
            data = data.setScale(decimal, RoundingMode.HALF_UP);
        }
        return data.stripTrailingZeros().toPlainString();
    }

    public static String formatDataAndStripZeros(BigDecimal data, Integer decimal) {
        return formatDataAndStripZeros(data, "-", decimal);
    }

    public static String formatDataAndStripZeros(BigDecimal data) {
        return formatDataAndStripZeros(data, "-", null);
    }

    /**
     * 数据格式化
     * 
     * @param data
     * @return
     */
    public static Double formatToDouble(BigDecimal data, Double defaultValue, Integer decimal) {
        if (data == null) {
            return defaultValue;
        }
        if (decimal != null) {
            data = data.setScale(decimal, RoundingMode.HALF_UP);
        }
        return data.doubleValue();
    }

    public static Double formatToDouble(BigDecimal data) {
        return formatToDouble(data, 0.0, null);
    }

    public static Double formatToDouble(BigDecimal data, Double defaultValue) {
        return formatToDouble(data, defaultValue, null);
    }

    public static Double formatToDouble(BigDecimal data, Integer decimal) {
        return formatToDouble(data, 0.0, decimal);
    }

    /**
     * 将两个BigDecimal相加
     * 
     * @param leftOperand  //加数
     * @param rightOperand //被加数
     * @param scale        //小数位数
     * @param flag         //零是是否格式化为0
     * @return
     */
    public static BigDecimal add(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale, boolean flag) {
        BigDecimal result = null;
        if (leftOperand == null) {
            if (rightOperand == null) {
                return null;
            }
            result = rightOperand;
        } else if (rightOperand == null) {
            result = leftOperand;
        } else {
            result = leftOperand.add(rightOperand);
        }

        if (scale != null) {
            result = result.setScale(scale, RoundingMode.HALF_UP);
        }

        if (flag && BigDecimal.ZERO.compareTo(result) == 0) {
            return BigDecimal.ZERO;
        }
        return result;
    }

    public static BigDecimal add(BigDecimal leftOperand, BigDecimal rightOperand) {
        return add(leftOperand, rightOperand, null, true);
    }

    public static BigDecimal add(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale) {
        return add(leftOperand, rightOperand, scale, true);
    }

    /**
     * 两数相减
     * 
     * @param leftOperand
     * @param rightOperand
     * @param scale
     * @param flag
     * @return
     */
    public static BigDecimal sub(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale, boolean flag) {
        BigDecimal result = null;
        if (leftOperand == null) {
            if (rightOperand == null) {
                return null;
            }
            result = BigDecimal.ZERO.subtract(rightOperand);
        } else if (rightOperand == null) {
            result = leftOperand;
        } else {
            result = leftOperand.subtract(rightOperand);
        }

        if (scale != null) {
            result = result.setScale(scale, RoundingMode.HALF_UP);
        }

        if (flag && BigDecimal.ZERO.compareTo(result) == 0) {
            return BigDecimal.ZERO;
        }
        return result;
    }

    public static BigDecimal sub(BigDecimal leftOperand, BigDecimal rightOperand) {
        return sub(leftOperand, rightOperand, null, true);
    }

    public static BigDecimal sub(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale) {
        return sub(leftOperand, rightOperand, scale, true);
    }

    /**
     * 两数相加
     * 
     * @param leftOperand  //左操作数
     * @param rightOperand //右操作数
     * @param scale        //小数位数
     * @param flag         //零是是否格式化为0
     * @return
     */
    public static BigDecimal addNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale, boolean flag) {
        if (leftOperand == null || rightOperand == null) {
            return null;
        }

        BigDecimal result = leftOperand.add(rightOperand);
        if (scale != null) {
            result = result.setScale(scale, RoundingMode.HALF_UP);
        }

        if (flag && BigDecimal.ZERO.compareTo(result) == 0) {
            return BigDecimal.ZERO;
        }
        return result;
    }

    public static BigDecimal addNotNull(BigDecimal leftOperand, BigDecimal rightOperand) {
        return addNotNull(leftOperand, rightOperand, null, true);
    }

    public static BigDecimal addNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale) {
        return addNotNull(leftOperand, rightOperand, scale, true);
    }

    /**
     * 两数相减
     * 
     * @param leftOperand
     * @param rightOperand
     * @param scale
     * @param flag         //零是是否格式化为0
     * @return
     */
    public static BigDecimal subNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale, boolean flag) {
        if (leftOperand == null || rightOperand == null) {
            return null;
        }

        BigDecimal result = leftOperand.subtract(rightOperand);
        if (scale != null) {
            result = result.setScale(scale, RoundingMode.HALF_UP);
        }

        if (flag && BigDecimal.ZERO.compareTo(result) == 0) {
            return BigDecimal.ZERO;
        }
        return result;
    }

    public static BigDecimal subNotNull(BigDecimal leftOperand, BigDecimal rightOperand) {
        return subNotNull(leftOperand, rightOperand, null, true);
    }

    public static BigDecimal subNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale) {
        return subNotNull(leftOperand, rightOperand, scale, true);
    }

    /**
     * 两数相除
     * 
     * @param leftOperand
     * @param rightOperand
     * @param scale
     * @param flag         //零是是否格式化为0
     * @param roundingMode
     * @return
     */
    public static BigDecimal divideNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale, boolean flag,
            RoundingMode roundingMode) {
        if (leftOperand == null || rightOperand == null || BigDecimal.ZERO.compareTo(rightOperand) == 0) {
            return null;
        }

        BigDecimal result = null;
        if (scale != null) {
            result = leftOperand.divide(rightOperand, scale, roundingMode);
        } else {
            result = leftOperand.divide(rightOperand, 12, roundingMode);
        }

        if (flag && BigDecimal.ZERO.compareTo(result) == 0) {
            return BigDecimal.ZERO;
        }
        return result;
    }

    public static BigDecimal divideNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale,
            boolean flag) {
        return divideNotNull(leftOperand, rightOperand, scale, flag, RoundingMode.HALF_UP);
    }

    public static BigDecimal divideNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale) {
        return divideNotNull(leftOperand, rightOperand, scale, true);
    }

    public static BigDecimal divideNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale,
            RoundingMode roundingMode) {
        return divideNotNull(leftOperand, rightOperand, scale, true, roundingMode);
    }

    public static BigDecimal divideNotNull(BigDecimal leftOperand, BigDecimal rightOperand) {
        return divideNotNull(leftOperand, rightOperand, 12);
    }

    /**
     * 两数相乘
     * 
     * @param leftOperand
     * @param rightOperand
     * @param scale
     * @param flag         //零是是否格式化为0
     * @return
     */
    public static BigDecimal multiplyNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale,
            boolean flag) {
        if (leftOperand == null || rightOperand == null) {
            return null;
        }

        BigDecimal result = null;
        if (scale != null) {
            result = leftOperand.multiply(rightOperand).setScale(scale, RoundingMode.HALF_UP);
        } else {
            result = leftOperand.multiply(rightOperand);
        }

        if (flag && BigDecimal.ZERO.compareTo(result) == 0) {
            return BigDecimal.ZERO;
        }
        return result;
    }

    public static BigDecimal multiplyNotNull(BigDecimal leftOperand, BigDecimal rightOperand, Integer scale) {
        return multiplyNotNull(leftOperand, rightOperand, scale, true);
    }

    public static BigDecimal multiplyNotNull(BigDecimal leftOperand, BigDecimal rightOperand) {
        return multiplyNotNull(leftOperand, rightOperand, null, true);
    }

    public static BigDecimal abs(BigDecimal data) {
        return data == null ? null : data.abs();
    }

    /**
     * 返回较大值
     * 
     * @param leftOperand
     * @param rightOperand
     * @return BigDecimal
     */
    public static BigDecimal max(BigDecimal leftOperand, BigDecimal rightOperand) {
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
    public static BigDecimal min(BigDecimal leftOperand, BigDecimal rightOperand) {
        if (leftOperand == null) {
            return rightOperand;
        }
        if (rightOperand == null) {
            return leftOperand;
        }

        return leftOperand.compareTo(rightOperand) < 0 ? leftOperand : rightOperand;
    }

    /**
     * 数字小数位数格式化
     * 
     * @return BigDecimal
     */
    public static BigDecimal numberDecimalFormat(BigDecimal number) {
        if (number == null) {
            return null;
        }
        if (BigDecimal.ZERO.compareTo(number) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal newNumber = number.abs();
        if (BigDecimal.ONE.compareTo(newNumber) < 0) {
            return number.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal standard = new BigDecimal(0.01);
        if (standard.compareTo(newNumber) < 0) {
            return newNumber.setScale(4, RoundingMode.HALF_UP);
        }
        standard = new BigDecimal(0.00001);
        if (standard.compareTo(newNumber) < 0) {
            return newNumber.setScale(6, RoundingMode.HALF_UP);
        }
        return newNumber.setScale(8, RoundingMode.HALF_UP);
    }

    public static BigDecimal numberDecimalFormatV2(BigDecimal number) {
        if (number == null) {
            return null;
        }
        if (BigDecimal.ZERO.compareTo(number) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal newNumber = number.abs();
        BigDecimal standard = new BigDecimal(0.1);
        if (standard.compareTo(newNumber) < 0) {
            return number.setScale(2, RoundingMode.HALF_UP);
        }
        standard = new BigDecimal(0.001);
        if (standard.compareTo(newNumber) < 0) {
            return newNumber.setScale(4, RoundingMode.HALF_UP);
        }
        standard = new BigDecimal(0.00001);
        if (standard.compareTo(newNumber) < 0) {
            return newNumber.setScale(6, RoundingMode.HALF_UP);
        }
        return newNumber.setScale(8, RoundingMode.HALF_UP);
    }
}