package com.stockmonitor.util;

import java.util.Set;

public class SetUtil {
    /**
     * 判断是否为空
     * 
     * @param set
     * @param <T>
     * @return boolean
     */
    public static <T> boolean isBlank(Set<T> set) {
        return set == null || set.isEmpty();
    }

    /**
     * 判断是否不为空
     * 
     * @param set
     * @param <T>
     * @return boolean
     */
    public static <T> boolean isNotBlank(Set<T> set) {
        return set != null && !set.isEmpty();
    }
}
