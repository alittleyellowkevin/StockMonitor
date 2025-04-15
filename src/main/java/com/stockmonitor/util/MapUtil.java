package com.stockmonitor.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    /**
     * 判断是否为空
     */
    public static boolean isEmpty(Map<String, Object> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断是否不为空
     */
    public static boolean isNotEmpty(Map<String, Object> map) {
        return map != null || !isEmpty(map);
    }

    /**
     * 根据map的值排序
     */
    public static Map<String, BigDecimal> sortMapByValue(Map<String, BigDecimal> map) {
        return sortMapByValue(map, DESC);
    }

    /**
     * 根据map的值排序
     */
    public static Map<String, BigDecimal> sortMapByValue(Map<String, BigDecimal> map, String order) {
        if (order == null) {
            order = DESC;
        }

        List<Map.Entry<String, BigDecimal>> listMap = new ArrayList<>(map.entrySet());
        LinkedHashMap<String, BigDecimal> resultMap = new LinkedHashMap<>();
        if (order.equals(ASC)) {
            Collections.sort(listMap, new MapValueAscComparator());
        } else {
            Collections.sort(listMap, new MapValueDescComparator());
        }

        for (Map.Entry<String, BigDecimal> entry : listMap) {
            resultMap.put(entry.getKey(), entry.getValue());
        }

        return resultMap;
    }

}

class MapValueAscComparator implements Comparator<Map.Entry<String, BigDecimal>> {
    @Override
    public int compare(Map.Entry<String, BigDecimal> leftOperand, Map.Entry<String, BigDecimal> rightOperand) {
        BigDecimal left = leftOperand.getValue() == null ? BigDecimal.ZERO : leftOperand.getValue();
        BigDecimal right = rightOperand.getValue() == null ? BigDecimal.ZERO : rightOperand.getValue();
        int flag = left.compareTo(right);
        if (flag == 0) {
            return leftOperand.getKey().compareTo(rightOperand.getKey());
        }
        return flag;
    }
}

class MapValueDescComparator implements Comparator<Map.Entry<String, BigDecimal>> {
    @Override
    public int compare(Map.Entry<String, BigDecimal> leftOperand, Map.Entry<String, BigDecimal> rightOperand) {
        BigDecimal left = leftOperand.getValue() == null ? BigDecimal.ZERO : leftOperand.getValue();
        BigDecimal right = rightOperand.getValue() == null ? BigDecimal.ZERO : rightOperand.getValue();
        int flag = right.compareTo(left);
        if (flag == 0) {
            return rightOperand.getKey().compareTo(leftOperand.getKey());
        }
        return flag;
    }
}
