package com.stockmonitor.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    /**
     * 判断是否不为空
     * 
     * @param list
     * @return boolean
     */
    public static <T> boolean isNotBlank(List<T> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * 判断是否为空
     * 
     * @param list
     * @return boolean
     */
    public static <T> boolean isBlank(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static List<String> getSameMembersLimitSize(List<String> list1, List<String> list2, int size) {
        if (list1 == null || list2 == null)
            return null;
        if (list1.isEmpty() || list2.isEmpty() || size <= 0)
            return new ArrayList<>();
        // list2 截取对应数量
        if (list2.size() > size) {
            list2 = list2.subList(0, size);
        }
        List<String> resList = new ArrayList<>();
        for (int i = 0; i < list1.size() && i < size; i++) {
            String key = list1.get(i);
            if (list2.contains(key)) {
                resList.add(key);
            }
        }
        return resList;
    }
}
