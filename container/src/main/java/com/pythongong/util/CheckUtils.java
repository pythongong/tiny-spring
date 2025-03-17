package com.pythongong.util;

public class CheckUtils {
    
    private CheckUtils() {}

    public static void nullArgs(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void emptyArray(Object[] array, String msg) {
        if (ClassUtils.isArrayEmpty(array)) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void emptyString(String str, String msg) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException(msg);
        }
    }
}
