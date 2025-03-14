package com.pythongong.util;

public class StringUtils {
    
    private StringUtils() {}

    public static boolean isNotNull(String str) {
        return str != null && str.isBlank();
    }
}
